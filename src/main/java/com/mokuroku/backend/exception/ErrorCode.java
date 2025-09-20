package com.mokuroku.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Members
  MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_MEMBER("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
  DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
  INVALID_PASSWORD("INVALID_PASSWORD", HttpStatus.UNAUTHORIZED),
  INVALID_CREDENTIALS("이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED), // ✅ 추가
  ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN),
  INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED),
  REDIS_ERROR("REDIS_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
  ACCOUNT_ALREADY_WITHDRAWN("ACCOUNT_ALREADY_WITHDRAWN", HttpStatus.BAD_REQUEST),
  ACCOUNT_SUSPENDED("정지된 계정입니다.", HttpStatus.FORBIDDEN),
  SOCIAL_ACCOUNT_ONLY("소셜 로그인 전용 계정입니다.", HttpStatus.BAD_REQUEST),


  // SNS
  NOT_FOUND_POST("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),

  // Comments
  NOT_FOUND_COMMENT("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
  INVALID_COMMENT_OWNERSHIP("해당 회원이 작성한 댓글이 아닙니다.", HttpStatus.FORBIDDEN),

  // Bookmark
  ALREADY_BOOKMARKED("이미 추가한 게시글입니다.", HttpStatus.CONFLICT),
  NOT_FOUND_BOOKMARK("존재하지 않는 북마크입니다.", HttpStatus.NOT_FOUND),
  INVALID_BOOKMARK_OWNERSHIP("해당 회원의 북마크가 아닙니다.", HttpStatus.FORBIDDEN),


  // BudgetBook
  NOT_FOUND_BUDGETBOOK("존재하지 않는 가계부입니다.", HttpStatus.NOT_FOUND),

  // Dutch

  // Products
  CRAWLING_UNKNOWN_ERROR("예기치 못한 크롤링 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_NATION_CODE("유효하지 않은 국가코드입니다. ", HttpStatus.BAD_REQUEST),
  NOT_FOUND_WISHLIST("존재하지 관심 상품입니다.", HttpStatus.NOT_FOUND),

  // Notification

  // Admin

  // Common
  PARSING_ERROR("파싱 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
  FAILED_IMAGE_SAVE("이미지 저장에 실패 했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  UN_SUPPORTED_IMAGE_TYPE("지원되지 않는 이미지 파일 형식입니다.", HttpStatus.BAD_REQUEST),
  IMAGE_IO_ERROR("파일이 없거나 접근할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_ACCESS_DENIED("권한이 없어 접근이 불가능한 이미지입니다.", HttpStatus.FORBIDDEN),
  IMAGE_NOT_HAVE_PATH("잘못된 이미지 파일 경로입니다.", HttpStatus.BAD_REQUEST),
  IMAGE_EXCEPTION("이미지 관련 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_INTERNAL_SERVER_ERROR("이미지 내부 서버 오류입니다.",HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_MALFORMED("잘못된 형식의 URL입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_NOT_FOUND("이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  SEND_MAIL_FAIL("메일전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_SEARCH_SCOPE("유효하지 않은 검색범위 입니다.", HttpStatus.BAD_REQUEST),

  // Redis 관련 예외 추가
  REDIS_CONNECTION_FAILED("Redis 서버에 연결할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  String message;
  HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
=======
package com.mokuroku.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // Members
  MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_MEMBER("이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
  DUPLICATE_NICKNAME("이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
  INVALID_PASSWORD("INVALID_PASSWORD", HttpStatus.UNAUTHORIZED),
  INVALID_CREDENTIALS("이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED), // ✅ 추가
  ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN),
  INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED),
  REDIS_ERROR("REDIS_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
  ACCOUNT_ALREADY_WITHDRAWN("ACCOUNT_ALREADY_WITHDRAWN", HttpStatus.BAD_REQUEST),
  ACCOUNT_SUSPENDED("정지된 계정입니다.", HttpStatus.FORBIDDEN),
  SOCIAL_ACCOUNT_ONLY("소셜 로그인 전용 계정입니다.", HttpStatus.BAD_REQUEST),


  // SNS
  NOT_FOUND_POST("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
  NOT_FOUND_LOCATION("존재하지 않는 위치입니다.", HttpStatus.NOT_FOUND),

  // Comments
  NOT_FOUND_COMMENT("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
  INVALID_COMMENT_OWNERSHIP("해당 회원이 작성한 댓글이 아닙니다.", HttpStatus.FORBIDDEN),

  // Bookmark
  ALREADY_BOOKMARKED("이미 추가한 게시글입니다.", HttpStatus.CONFLICT),
  NOT_FOUND_BOOKMARK("존재하지 않는 북마크입니다.", HttpStatus.NOT_FOUND),
  INVALID_BOOKMARK_OWNERSHIP("해당 회원의 북마크가 아닙니다.", HttpStatus.FORBIDDEN),


  // BudgetBook
  NOT_FOUND_BUDGETBOOK("존재하지 않는 가계부입니다.", HttpStatus.NOT_FOUND),

  // Dutch

  // Products
  CRAWLING_UNKNOWN_ERROR("예기치 못한 크롤링 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_NATION_CODE("유효하지 않은 국가코드입니다. ", HttpStatus.BAD_REQUEST),
  NOT_FOUND_WISHLIST("존재하지 관심 상품입니다.", HttpStatus.NOT_FOUND),

  // Notification

  // Admin

  // Common
  PARSING_ERROR("파싱 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
  FAILED_IMAGE_SAVE("이미지 저장에 실패 했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  UN_SUPPORTED_IMAGE_TYPE("지원되지 않는 이미지 파일 형식입니다.", HttpStatus.BAD_REQUEST),
  IMAGE_IO_ERROR("파일이 없거나 접근할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_ACCESS_DENIED("권한이 없어 접근이 불가능한 이미지입니다.", HttpStatus.FORBIDDEN),
  IMAGE_NOT_HAVE_PATH("잘못된 이미지 파일 경로입니다.", HttpStatus.BAD_REQUEST),
  IMAGE_EXCEPTION("이미지 관련 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_INTERNAL_SERVER_ERROR("이미지 내부 서버 오류입니다.",HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_MALFORMED("잘못된 형식의 URL입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  IMAGE_NOT_FOUND("이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  SEND_MAIL_FAIL("메일전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_SEARCH_SCOPE("유효하지 않은 검색범위 입니다.", HttpStatus.BAD_REQUEST),

  // Redis 관련 예외 추가
  REDIS_CONNECTION_FAILED("Redis 서버에 연결할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  String message;
  HttpStatus status;

  ErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}
