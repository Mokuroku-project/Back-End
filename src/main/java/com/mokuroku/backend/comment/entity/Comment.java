package com.mokuroku.backend.comment.entity;

import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.comment.type.Visibility;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostEntity post;

  @ManyToOne
  @JoinColumn(name = "email")
  private Member member;

  private String content;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private LocalDateTime deletedDate;

  @Enumerated(EnumType.STRING)
  private Visibility visibility;

  @Enumerated(EnumType.STRING)
  private CommentStatus status;
}
