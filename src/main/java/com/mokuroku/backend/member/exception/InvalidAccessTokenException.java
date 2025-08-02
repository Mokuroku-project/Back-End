package com.mokuroku.backend.member.exception;

public class InvalidAccessTokenException extends RuntimeException {
  public InvalidAccessTokenException() {
    super();
  }

  public InvalidAccessTokenException(String message) {
    super(message);
  }

  public InvalidAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidAccessTokenException(Throwable cause) {
    super(cause);
  }
}
