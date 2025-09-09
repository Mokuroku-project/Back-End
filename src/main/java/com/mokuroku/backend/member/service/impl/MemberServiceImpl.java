package com.mokuroku.backend.member.service.impl;

import com.mokuroku.backend.common.Constants;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.JwtTokenProvider;
import com.mokuroku.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO, MultipartFile file) {
        // 중복 이메일 검사
        if (memberRepository.existsById(requestDTO.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER);
        }

        // 중복 닉네임 검사
        if (memberRepository.existsByNickname(requestDTO.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        // 프로필 이미지 처리
        String profileImage = null;
        if (file != null && !file.isEmpty()) {
            try {
                if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
                    throw new CustomException(ErrorCode.FILE_TOO_LARGE);
                }
                if (!file.getContentType().startsWith("image/")) {
                    throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
                }
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads/profiles/" + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                profileImage = filePath.toString();
            } catch (IOException e) {
                log.error("Failed to upload profile image", e);
                throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        // Member 엔티티 생성
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(encodedPassword)
                .nickname(requestDTO.getNickname())
                .profileImage(profileImage)
                .socialLoginCheck(requestDTO.isSocialLoginCheck() ? Member.SocialLogin.SOCIAL : Member.SocialLogin.GENERAL)
                .regDate(LocalDateTime.now())
                .role(Member.Role.USER)
                .status(Member.Status.USABLE)
                .build();

        // 저장
        Member saved = memberRepository.save(member);

        // 응답 반환
        return RegisterResponseDTO.builder()
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .profileImage(saved.getProfileImage())
                .role(saved.getRole().name().toLowerCase())
                .regDate(saved.getRegDate())
                .build();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findById(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (member.getStatus() == Member.Status.UNUSABLE) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        redisTemplate.opsForValue().set(
                Constants.REFRESH_PREFIX + member.getEmail(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpiration(),
                TimeUnit.MILLISECONDS
        );

        return LoginResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        log.info("Logout request for email: {}", email);
        redisTemplate.delete(Constants.REFRESH_PREFIX + email);
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        if (expiration > 0) {
            redisTemplate.opsForValue().set(
                    Constants.BLACKLIST_PREFIX + accessToken,
                    "logout",
                    expiration,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    @Override
    public void withdraw(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        log.info("Withdrawal request for email: {}", email);

        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (member.getStatus() == Member.Status.UNUSABLE) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_WITHDRAWN);
        }

        // 계정 비활성화 및 탈퇴 일시 설정
        member.setStatus(Member.Status.UNUSABLE);
        member.setWithdrawalDate(LocalDateTime.now());
        memberRepository.save(member);

        // Redis에서 리프레시 토큰 삭제 및 액세스 토큰 블랙리스트 추가
        redisTemplate.delete(Constants.REFRESH_PREFIX + email);
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        if (expiration > 0) {
            redisTemplate.opsForValue().set(
                    Constants.BLACKLIST_PREFIX + accessToken,
                    "withdrawal",
                    expiration,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    private MemberDTO toMemberDTO(Member member) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .socialLogin(member.getSocialLoginCheck() == Member.SocialLogin.SOCIAL)
                .regDate(member.getRegDate())
                .withdrawalDate(member.getWithdrawalDate())
                .role(MemberDTO.Role.valueOf(member.getRole().name()))
                .status(member.getStatus() == Member.Status.USABLE ? MemberDTO.Status.USABLE : MemberDTO.Status.UNUSABLE)
                .build();
    }
}