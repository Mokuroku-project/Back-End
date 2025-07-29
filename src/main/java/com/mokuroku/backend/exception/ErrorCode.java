package com.mokuroku.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Members
  NOT_FOUND_MEMBER("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);

  // SNS

  // Comments

  // Bookmark

  // BudgetBook

  // Dutch

  // Products

  // Notification

  // Admin


  String message;
  HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
