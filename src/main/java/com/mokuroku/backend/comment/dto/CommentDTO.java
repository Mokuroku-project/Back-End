package com.mokuroku.backend.comment.dto;

import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.comment.type.Visibility;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import java.time.LocalDateTime;
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

  public static Comment createComment(CommentDTO dto, PostEntity post, Member member) {
    return Comment.builder()
        .commentId(dto.getCommentId())
        .post(post)
        .email(member)
        .content(dto.getContent())
        .regDate(LocalDateTime.now())
        .visibility(Visibility.valueOf(dto.getVisibility().toUpperCase()))
        .status(CommentStatus.valueOf(String.valueOf(CommentStatus.POSTED)))
        .build();
  }
}
