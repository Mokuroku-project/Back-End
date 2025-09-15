package com.mokuroku.backend.comment.dto;

import com.mokuroku.backend.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDTO {

  private Long commentId;
  private Long postId;
  private String nickname;
  private String content;
  private LocalDateTime regDate;
  private String visibility;
  private String status;

  public static CommentDTO toDTO(Comment comment) {
    return CommentDTO.builder()
        .commentId(comment.getCommentId())
        .postId(comment.getPost().getPostId())
        .nickname(comment.getEmail().getNickname())
        .content(comment.getContent())
        .regDate(comment.getRegDate())
        .visibility(comment.getVisibility().toString())
        .status(comment.getStatus().toString())
        .build();
  }
}
