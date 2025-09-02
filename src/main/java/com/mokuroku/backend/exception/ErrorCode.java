package com.mokuroku.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Members
  NOT_FOUND_MEMBER("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_MEMBER("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
  DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),

  // SNS
  NOT_FOUND_POST("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
  // Comments

  // Bookmark

  // BudgetBook
  NOT_FOUND_BUDGETBOOK("존재하지 않는 가계부입니다.", HttpStatus.NOT_FOUND),

  // Dutch

  // Products
  CRAWLING_UNKNOWN_ERROR("예기치 못한 크롤링 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_NATION_CODE("유효하지 않은 국가코드입니다. ", HttpStatus.BAD_REQUEST),
  NOT_FOUND_WISHLIST("존재하지 관심 상품입니다.", HttpStatus.NOT_FOUND);

  // Notification

  // Admin


  String message;
  HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
