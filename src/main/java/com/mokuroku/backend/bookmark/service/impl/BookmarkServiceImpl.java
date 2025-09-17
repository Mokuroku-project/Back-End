package com.mokuroku.backend.bookmark.service.impl;

import com.mokuroku.backend.bookmark.dto.BookmarkDTO;
import com.mokuroku.backend.bookmark.dto.BookmarkRequestDTO;
import com.mokuroku.backend.bookmark.entity.Bookmark;
import com.mokuroku.backend.bookmark.repository.BookmarkRepository;
import com.mokuroku.backend.bookmark.service.BookmarkService;
import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

  private final MemberRepository memberRepository;
  private final PostRepository postRepository;
  private final BookmarkRepository bookmarkRepository;

  @Override
  public ResponseEntity<ResultDTO> addBookmark(BookmarkRequestDTO bookmarkRequestDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Long postId = bookmarkRequestDTO.getPostId();
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    bookmarkRepository.findByEmailAndPostId(member, post)
        .ifPresent(b -> {
          throw new CustomException(ErrorCode.ALREADY_BOOKMARKED);
        });

    Bookmark bookmark = BookmarkDTO.toEntity(member, post);
    bookmarkRepository.save(bookmark);

    return ResponseEntity.ok(new ResultDTO<>("북마크 저장에 성공했습니다.", null));
  }

  @Override
  public ResponseEntity<ResultDTO> getBookmark() {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    List<Bookmark> bookmarks = bookmarkRepository.findByEmail(member);
    List<BookmarkDTO> bookmarkDTOS = bookmarks.stream()
        .map(BookmarkDTO::toDTO)
        .toList();

    return ResponseEntity.ok(new ResultDTO<>("북마크 조회를 성공했습니다.", bookmarkDTOS));
  }

  @Override
  public ResponseEntity<ResultDTO> deleteBookmark(BookmarkRequestDTO bookmarkRequestDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Bookmark bookmark = bookmarkRepository.findById(bookmarkRequestDTO.getBookmarkId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOKMARK));

    if (bookmark.getEmail().equals(member)) {
      bookmarkRepository.delete(bookmark);
    } else {
      throw new CustomException(ErrorCode.INVALID_BOOKMARK_OWNERSHIP);
    }

    return ResponseEntity.ok(new ResultDTO<>("북마크 삭제에 성공했습니다.", null));
  }
}
