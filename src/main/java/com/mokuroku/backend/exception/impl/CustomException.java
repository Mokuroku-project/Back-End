package com.mokuroku.backend.exception.impl;

import com.mokuroku.backend.exception.AbstractException;
import com.mokuroku.backend.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CustomException extends AbstractException {

  private String message;
  private HttpStatus status;
  private ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
    this.status = errorCode.getStatus();
  }

  @Override
  public HttpStatus getHttpStatus() {
    return null;
  }

  @Override
  public ErrorCode getErrorCode() {
    return null;
  }

  @Override
  public String getMessage() {
    return "";
  }
}
