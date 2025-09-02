package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
//import com.mokuroku.backend.member.security.JwtTokenProvider;
import com.mokuroku.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final StringRedisTemplate redisTemplate;

//    private static final String BLACKLIST_PREFIX = "blacklist:";
//    private static final String REFRESH_PREFIX = "refresh:";

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {
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

        // Member 엔티티 생성
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(encodedPassword)
                .nickname(requestDTO.getNickname())
                .profileImage(requestDTO.getProfileImage())
                .socialLoginCheck(requestDTO.isSocialLoginCheck() ? "Y" : "N")
                .regDate(LocalDateTime.now())
                .role(Member.Role.USER)
                .status("1")
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
}

//    @Override
//    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
//        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
//
//        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
//        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());
//
//        redisTemplate.opsForValue().set(
//                REFRESH_PREFIX + member.getEmail(),
//                refreshToken,
//                jwtTokenProvider.getRefreshExpirationMs(),
//                TimeUnit.MILLISECONDS
//        );
//
//        return LoginResponseDTO.builder()
//                .userId(member.getId())
//                .email(member.getEmail())
//                .nickname(member.getNickname())
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

//    @Override
//    public void logout(String accessToken) {
//        if (!jwtTokenProvider.validateToken(accessToken)) {
//            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
//        }
//
//        String email = jwtTokenProvider.getEmailFromToken(accessToken);
//        log.info("로그아웃 요청 이메일: {}", email);
//
//        long expiration = jwtTokenProvider.getExpiration(accessToken);
//        redisTemplate.opsForValue().set(
//                BLACKLIST_PREFIX + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
//
//        redisTemplate.delete(REFRESH_PREFIX + email);
//    }

/*
사용된 컴포넌트 요약
컴포넌트	설명
PasswordEncoder	비밀번호 해시 비교
JwtTokenProvider	JWT 액세스 토큰 발급
MemberRepository	사용자 조회용 JPA Repository

💡 추가 고려사항
로그인 실패 횟수 제한 (보안)
계정 상태 체크 (status가 unusable이면 로그인 막기)
소셜 로그인 분기
Refresh Token 발급 및 Redis 저장
*/