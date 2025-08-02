package com.mokuroku.backend.member.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.warn("인증 실패: {}", authException.getMessage());

        // 401 Unauthorized 반환
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
    }
}

/*
JwtAuthenticationEntryPoint는 인증되지 않은 사용자가 보호된 리소스에 접근했을 때
Spring Security가 401 Unauthorized 응답을 반환하도록 처리하는 클래스입니다.

✅ 역할
상황	처리 내용
사용자가 인증 없이 보호된 API에 접근	commence() 메서드 호출됨
예: /api/users/me 호출 시 JWT 없거나 위조됨	401 Unauthorized 응답 반환
*/