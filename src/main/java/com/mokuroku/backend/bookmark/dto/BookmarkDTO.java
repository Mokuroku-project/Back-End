package com.mokuroku.backend.bookmark.dto;

import com.mokuroku.backend.bookmark.entity.Bookmark;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDTO {

  private Long bookmarkId;
  private String email;
  private Long postId;

  public static Bookmark toEntity(Member member, PostEntity post) {
    return Bookmark.builder()
        .email(member)
        .postId(post)
        .build();
  }
}
