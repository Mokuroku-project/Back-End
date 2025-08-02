package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.member.dto.TokenReissueRequestDTO;
import com.mokuroku.backend.member.dto.TokenResponseDTO;
import com.mokuroku.backend.member.security.JwtTokenProvider;
import com.mokuroku.backend.member.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    private static final String REFRESH_PREFIX = "refresh:";

    @Override
    public TokenResponseDTO reissueToken(TokenReissueRequestDTO requestDTO) {
        String accessToken = requestDTO.getAccessToken();
        String refreshToken = requestDTO.getRefreshToken();

        // ✅ 1. Refresh Token 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        // ✅ 2. Access Token에서 이메일(subject) 추출 (만료되어도 가능)
        String email = jwtTokenProvider.getEmailFromToken(accessToken);

        // ✅ 3. Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_PREFIX + email);
        if (!StringUtils.hasText(storedRefreshToken) || !storedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않거나 존재하지 않습니다.");
        }

        // ✅ 4. 새로운 토큰 재발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        // ✅ 5. Redis 갱신
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + email,
                newRefreshToken,
                jwtTokenProvider.getRefreshExpirationMs(),
                TimeUnit.MILLISECONDS
        );

        // ✅ 6. 결과 반환
        return TokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}