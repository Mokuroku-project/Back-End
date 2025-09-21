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
        .member(member)
        .postId(post)
        .build();
  }

  public static BookmarkDTO toDTO(Bookmark bookmark) {
    return BookmarkDTO.builder()
        .bookmarkId(bookmark.getBookmarkId())
        .email(bookmark.getMember().getEmail())
        .postId(bookmark.getPostId().getPostId())
        .build();
  }
}
