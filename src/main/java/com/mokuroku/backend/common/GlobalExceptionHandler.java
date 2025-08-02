package com.mokuroku.backend.common;

import com.mokuroku.backend.member.exception.DuplicateEmailException;
import com.mokuroku.backend.member.exception.InvalidAccessTokenException;
import com.mokuroku.backend.member.exception.InvalidLoginException;
import com.mokuroku.backend.member.exception.UnsupportedProviderException;
import com.mokuroku.backend.common.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponseDTO buildError(HttpStatus status, String message, String path) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    // ✅ 공통: 커스텀 예외
    @ExceptionHandler({
            DuplicateEmailException.class,
            InvalidLoginException.class,
            InvalidAccessTokenException.class,
            UnsupportedProviderException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleCustomRuntimeExceptions(RuntimeException ex, HttpServletRequest request) {
        log.warn("Custom Exception: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    // ✅ 인증 실패 - JWT 관련
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleSecurity(SecurityException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "인증 오류: " + ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", request.getRequestURI());
    }

    // ✅ 인가 실패
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", request.getRequestURI());
    }

    // ✅ 유효성 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "입력값 오류";
        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    // ✅ 예상 못한 서버 오류
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleAllUnhandled(Exception ex, HttpServletRequest request) {
        log.error("서버 내부 오류 발생", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", request.getRequestURI());
    }
}

/*
이유: 애플리케이션 전역에서 발생하는 예외를 처리하므로 도메인과 독립적인 별도의 공통(common) 패키지에 두는 것이 관리와 재사용에 유리함.
만약 프로젝트 규모가 작거나 도메인 수가 적다면 com.example.exception 같이 단일 패키지로도 충분함.

@RestControllerAdvice: 모든 REST 컨트롤러에 적용되는 글로벌 예외 처리기
커스텀 예외별로 HTTP 상태코드와 메시지 반환
유효성 검사 실패 시 필드별 상세 메시지 전달
예외 종류에 따라 적절한 HTTP 상태코드 반환
알 수 없는 예외는 500 서버 오류로 처리
*/