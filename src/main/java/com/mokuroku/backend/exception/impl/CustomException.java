package com.mokuroku.backend.exception.impl;

import com.mokuroku.backend.exception.AbstractException;
import com.mokuroku.backend.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CustomException extends AbstractException {

  private final String message;
  private final HttpStatus status;
  private final ErrorCode errorCode;

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
