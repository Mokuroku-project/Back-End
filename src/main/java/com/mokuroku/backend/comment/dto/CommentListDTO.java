package com.mokuroku.backend.comment.dto;

import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.entity.ReplyComment;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.comment.type.Visibility;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CommentListDTO {

  private Long commentId;
  private Long postId;
  private String nickname;
  private String content;
  private LocalDateTime regDate;
  private String visibility;
  private String status;
  private List<ReplyCommentDTO> replyComments;

  public static CommentListDTO toDTO(Comment comment, List<ReplyCommentDTO> replyComments) {
    return CommentListDTO.builder()
        .commentId(comment.getCommentId())
        .postId(comment.getPost().getPostId())
        .nickname(comment.getMember().getNickname())
        .content(comment.getContent())
        .regDate(comment.getRegDate())
        .visibility(comment.getVisibility().toString())
        .status(comment.getStatus().toString())
            .replyComments(replyComments)
        .build();
  }
}
