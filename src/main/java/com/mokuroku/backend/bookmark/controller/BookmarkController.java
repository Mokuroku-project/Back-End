package com.mokuroku.backend.bookmark.controller;

import com.mokuroku.backend.bookmark.service.BookmarkService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ResultDTO> addBookmark(@RequestBody Long postId) {
    ResponseEntity<ResultDTO> result = bookmarkService.addBookmark(postId);
    return result;
  }
}
