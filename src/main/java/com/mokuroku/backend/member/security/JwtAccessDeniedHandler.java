package com.mokuroku.backend.member.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.warn("권한 부족: {}", accessDeniedException.getMessage());

        // 403 Forbidden 반환
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
    }
}

/*
JwtAccessDeniedHandler는 인증은 되었지만 권한이 부족할 때
Spring Security가 403 Forbidden 응답을 반환하도록 처리하는 핸들러입니다.

✅ 역할 요약
상황	처리 내용
인증은 되었지만 필요한 권한 부족 (예: ROLE_ADMIN 없음)	403 Forbidden 반환
인증 자체가 없으면 → JwtAuthenticationEntryPoint 가 처리	✅
 */