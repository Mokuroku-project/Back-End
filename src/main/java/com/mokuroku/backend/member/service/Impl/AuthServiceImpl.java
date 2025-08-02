package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.JwtTokenProvider;
import com.mokuroku.backend.member.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String REFRESH_PREFIX = "refresh:";

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());

        // Redis에 Refresh Token 저장
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + member.getEmail(),
                refreshToken,
                jwtTokenProvider.getRefreshExpirationMs(),
                TimeUnit.MILLISECONDS
        );

        return LoginResponseDTO.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        // 1. 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 2. 이메일 추출 및 로그 출력
        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        log.info("로그아웃 요청 이메일: {}", email);

        // 3. 블랙리스트 등록
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(BLACKLIST_PREFIX + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        // 4. RefreshToken 삭제
        redisTemplate.delete(REFRESH_PREFIX + email);
    }
}



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