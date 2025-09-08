package com.mokuroku.backend.bookmark.entity;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "bookmark")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

  @Id
  private Long BookmarkId;

  @ManyToOne
  @JoinColumn(name = "email")
  private Member email;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostEntity postId;
}
