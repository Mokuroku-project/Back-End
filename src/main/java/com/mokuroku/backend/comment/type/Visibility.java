package com.mokuroku.backend.comment.type;

public enum Visibility {
  ALL("전체 공개"),
  AUTHOR_AND_ME("작성자와 나만 보기");

  private final String description;

  Visibility(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
