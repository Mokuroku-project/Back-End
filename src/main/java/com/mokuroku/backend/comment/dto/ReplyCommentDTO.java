package com.mokuroku.backend.comment.dto;

import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.entity.ReplyComment;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.comment.type.Visibility;
import com.mokuroku.backend.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyCommentDTO {

    private Long replyCommentId;
    private Long commentId;
    private String content;
    private LocalDateTime regDate;
    private String status;
    private String visibility;

    public static ReplyComment createReplyComment(
            ReplyCommentDTO replyCommentDTO, Comment comment, Member member) {

        return ReplyComment.builder()
                .comment(comment)
                .member(member)
                .content(replyCommentDTO.getContent())
                .regDate(LocalDateTime.now())
                .visibility(Visibility.valueOf(replyCommentDTO.getVisibility().toUpperCase()))
                .status(CommentStatus.valueOf(String.valueOf(CommentStatus.POSTED)))
                .build();
    }

    public static ReplyCommentDTO toDTO(ReplyComment replyComment) {

        return ReplyCommentDTO.builder()
                .replyCommentId(replyComment.getReplyCommentId())
                .commentId(replyComment.getComment().getCommentId())
                .content(replyComment.getContent())
                .regDate(replyComment.getRegDate())
                .status(replyComment.getStatus().toString())
                .visibility(replyComment.getVisibility().toString())
                .build();
    }
}
