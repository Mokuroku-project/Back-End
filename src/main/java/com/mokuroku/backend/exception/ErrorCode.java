package com.mokuroku.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * 에러 코드를 정의하는 열거형.
 * 메시지는 MessageSource에서 동적으로 조회, HTTP 상태 코드만 정의.
 */
@Getter
public enum ErrorCode {

  // Members
  NOT_FOUND_MEMBER("error.not_found_member", HttpStatus.NOT_FOUND),
  DUPLICATE_EMAIL("error.duplicate_email", HttpStatus.CONFLICT),
  DUPLICATE_NICKNAME("error.duplicate_nickname", HttpStatus.CONFLICT),
  INVALID_TOKEN("error.invalid_token", HttpStatus.BAD_REQUEST),
  ALREADY_VERIFIED("error.already_verified", HttpStatus.BAD_REQUEST),
  EXPIRED_TOKEN("error.expired_token", HttpStatus.BAD_REQUEST),
  EMAIL_SEND_FAILED("error.email_send_failed", HttpStatus.INTERNAL_SERVER_ERROR),
  RESEND_LIMIT_EXCEEDED("error.resend_limit_exceeded", HttpStatus.BAD_REQUEST),
  TOO_FREQUENT_RESEND("error.too_frequent_resend", HttpStatus.TOO_MANY_REQUESTS),
  TOO_FREQUENT_EMAIL("error.too_frequent_email", HttpStatus.TOO_MANY_REQUESTS),
  PASSWORD_REQUIRED("error.password_required", HttpStatus.BAD_REQUEST),
  INVALID_EMAIL("error.invalid_email", HttpStatus.BAD_REQUEST),

  // SNS
  POST_NOT_FOUND("error.post_not_found", HttpStatus.BAD_REQUEST),

  // Comments

  // Bookmark

  // BudgetBook
  NOT_FOUND_BUDGETBOOK("error.not_found_budgetbook", HttpStatus.NOT_FOUND);

  // Dutch

  // Products

  // Notification

  // Admin

  private final String message;
  private final HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
