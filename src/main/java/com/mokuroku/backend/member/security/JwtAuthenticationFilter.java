package com.mokuroku.backend.member.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 매 요청마다 Authorization 헤더의 Bearer 토큰을 읽어
 * JwtTokenProvider로 검증하고, 성공 시 SecurityContext에 Authentication을 세팅하는 필터.
 *
 * - 유효하지 않거나 토큰이 없는 경우: 컨텍스트를 세팅하지 않고 체인을 계속 진행
 *   (보호된 URL이면 이후 FilterSecurityInterceptor 단계에서 401/403 처리)
 * - 예외 발생 시: 컨텍스트를 비우고 체인 진행 (EntryPoint가 최종 응답을 만듦)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 이미 인증된 컨텍스트가 있으면 스킵 (체인만 진행)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Bearer 토큰 추출
        String token = resolveBearerToken(request);

        if (token != null) {
            try {
                // 토큰 파싱 + 서명/만료/issuer 검증
                Jws<Claims> jws = jwt.parseAndValidate(token);

                // 주체(이메일)와 권한(role) 추출
                String email = jws.getPayload().getSubject();
                String role  = asString(jws.getPayload().get("role")); // "USER" / "ADMIN" 등

                // ROLE_ 접두로 SimpleGrantedAuthority 생성
                List<SimpleGrantedAuthority> authorities =
                        role != null ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                                : List.of();

                // principal로 email만 쓰거나, 필요하면 UserDetails로 교체 가능
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // 현재 요청 스레드 컨텍스트에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException ex) {
                // 만료된 토큰: 컨텍스트 비우고 체인 계속
                SecurityContextHolder.clearContext();
                // 선택) 클라이언트 디버깅을 돕는 힌트(표준은 아니므로 운영에선 생략 가능)
                response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\", error_description=\"expired\"");
            } catch (Exception ex) {
                // 서명 위조/형식 오류 등 모든 검증 실패 케이스
                SecurityContextHolder.clearContext();
                response.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            }
        }

        // 다음 필터로 진행
        chain.doFilter(request, response);
    }

    /** Authorization: Bearer xxx 에서 토큰만 추출 */
    @Nullable
    private String resolveBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) return null;
        if (!header.startsWith("Bearer ")) return null;
        String token = header.substring(7).trim();
        return token.isEmpty() ? null : token;
    }

    @Nullable
    private String asString(Object v) {
        return v != null ? v.toString() : null;
    }
}
