package com.mokuroku.backend.exception.impl;

import com.mokuroku.backend.exception.AbstractException;
import com.mokuroku.backend.exception.ErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class CustomException extends AbstractException {
  private final String message;
  private final HttpStatus status;
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode, MessageSource messageSource) {
    this.errorCode = errorCode;
    this.message = messageSource.getMessage(errorCode.getMessage(), null, LocaleContextHolder.getLocale());
    this.status = errorCode.getStatus();
  }

  public CustomException(ErrorCode errorCode, MessageSource messageSource, Object[] args) {
    this.errorCode = errorCode;
    this.message = messageSource.getMessage(errorCode.getMessage(), args, LocaleContextHolder.getLocale());
    this.status = errorCode.getStatus();
  }

  // 새로운 생성자: MessageSource 없이 ErrorCode의 기본 메시지 사용
  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
    this.status = errorCode.getStatus();
  }

  @Override
  public HttpStatus getHttpStatus() {
    return status;
  }

  @Override
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}