package com.mokuroku.backend.comment.entity;

import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.comment.type.Visibility;
import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "reply_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReplyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyCommentId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "email")
    private Member member;

    private String content;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
