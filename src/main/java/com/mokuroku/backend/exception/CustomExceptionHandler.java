package com.mokuroku.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(AbstractException.class)
  protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(e.getHttpStatus())
        .errorCode(e.getErrorCode())
        .message(e.getMessage())
        .build();

    return new ResponseEntity<>(errorResponse, e.getHttpStatus());
  }
}