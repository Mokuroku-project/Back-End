package com.mokuroku.backend.sns.entity;

public enum PostStatus {
    ACTIVE("게시"),
    DELETED("삭제");

    private final String description;

    PostStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
