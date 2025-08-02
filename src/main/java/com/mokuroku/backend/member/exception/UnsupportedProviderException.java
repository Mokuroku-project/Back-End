package com.mokuroku.backend.member.exception;

public class UnsupportedProviderException extends RuntimeException {
  public UnsupportedProviderException() {
    super();
  }

  public UnsupportedProviderException(String message) {
    super(message);
  }

  public UnsupportedProviderException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedProviderException(Throwable cause) {
    super(cause);
  }
}
