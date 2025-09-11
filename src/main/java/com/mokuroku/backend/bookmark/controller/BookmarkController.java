package com.mokuroku.backend.bookmark.controller;

import com.mokuroku.backend.bookmark.dto.BookmarkRequestDTO;
import com.mokuroku.backend.bookmark.service.BookmarkService;
import com.mokuroku.backend.common.ResultDTO;
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
    ResponseEntity<ResultDTO> result = bookmarkService.addBookmark(bookmarkRequestDTO);
    return result;
  }

  @GetMapping()
  public ResponseEntity<ResultDTO> getBookmark() {
    ResponseEntity<ResultDTO> result = bookmarkService.getBookmark();
    return result;
  }

  @DeleteMapping()
  public ResponseEntity<ResultDTO> deleteBookmark(
      @RequestBody BookmarkRequestDTO bookmarkRequestDTO) {
    ResponseEntity<ResultDTO> result = bookmarkService.deleteBookmark(bookmarkRequestDTO);
    return result;
  }
}
