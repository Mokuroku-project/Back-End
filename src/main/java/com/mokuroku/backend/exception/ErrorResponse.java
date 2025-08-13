package com.mokuroku.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mokuroku.backend.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * API 에러 응답을 나타내는 DTO 클래스.
 * 에러 발생 시 HTTP 상태, 에러 코드, 메시지, 타임스탬프를 포함하여 클라이언트에 반환.
 */
@Builder
@Getter
public class ErrorResponse {

  /**
   * 에러 발생 시간.
   * ISO 8601 형식으로 포맷 (예: "2025-08-13T21:19:00").
   */
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  /**
   * HTTP 상태 코드.
   * 예: HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND
   */
  private HttpStatus status;

  /**
   * 에러 코드.
   * 예: EMAIL_ALREADY_EXISTS, INVALID_TOKEN
   */
  private ErrorCode errorCode;

  /**
   * 에러 메시지.
   * 예: "이미 존재하는 이메일입니다."
   */
  private String message;
}