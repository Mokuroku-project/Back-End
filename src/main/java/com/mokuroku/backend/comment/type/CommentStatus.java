package com.mokuroku.backend.comment.type;

public enum CommentStatus {
  POSTED("게시"),
  DELETED("삭제");

  private final String description;

  CommentStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}