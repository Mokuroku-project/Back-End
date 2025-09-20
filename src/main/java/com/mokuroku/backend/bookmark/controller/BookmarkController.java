package com.mokuroku.backend.bookmark.controller;

import com.mokuroku.backend.bookmark.dto.BookmarkDTO;
import com.mokuroku.backend.bookmark.dto.BookmarkRequestDTO;
import com.mokuroku.backend.bookmark.service.BookmarkService;
import com.mokuroku.backend.common.ResultDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  @PostMapping()
  public ResponseEntity<ResultDTO> addBookmark(@RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
    bookmarkService.addBookmark(bookmarkRequestDTO);
    return ResponseEntity.ok(new ResultDTO<>("북마크 저장에 성공했습니다.", null));
  }

  @GetMapping()
  public ResponseEntity<ResultDTO<List<BookmarkDTO>>> getBookmark() {
    List<BookmarkDTO> result = bookmarkService.getBookmark();
    return ResponseEntity.ok(new ResultDTO<>("북마크 조회를 성공했습니다.", result));
  }

  @DeleteMapping()
  public ResponseEntity<ResultDTO> deleteBookmark(
      @RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
    bookmarkService.deleteBookmark(bookmarkRequestDTO);
    return ResponseEntity.ok(new ResultDTO<>("북마크 삭제에 성공했습니다.", null));
  }
}
