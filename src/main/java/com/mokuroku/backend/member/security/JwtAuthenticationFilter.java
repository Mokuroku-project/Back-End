package com.mokuroku.backend.member.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token)) {
            try {
                // 🔒 블랙리스트 확인
                if (isBlacklisted(token)) {
                    log.warn("❌ 차단된 토큰 사용 시도: {}", token);
                    throw new SecurityException("해당 토큰은 로그아웃된 토큰입니다.");
                }

                // ✅ 토큰 유효성 검사
                if (jwtTokenProvider.validateToken(token)) {
                    String userEmail = jwtTokenProvider.getEmailFromToken(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userEmail, null, null);

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                log.error("⚠️ JWT 인증 처리 중 오류: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 제거
        }
        return null;
    }

    /**
     * Redis 블랙리스트 토큰 여부 확인
     */
    private boolean isBlacklisted(String token) {
        return redisTemplate.opsForValue().get("blacklist:" + token) != null;
    }
}

/*
✅ JwtAuthenticationFilter 역할
Authorization 헤더에서 Bearer {token} 추출
JwtTokenProvider를 이용해 토큰 유효성 검사
유효하다면 UsernamePasswordAuthenticationToken 생성 후 SecurityContext에 주입
다음 필터 체인으로 진행
*/