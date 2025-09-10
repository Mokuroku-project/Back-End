package com.mokuroku.backend.bookmark.service;

import com.mokuroku.backend.bookmark.dto.BookmarkRequestDTO;
import com.mokuroku.backend.common.ResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkService {

  ResponseEntity<ResultDTO> addBookmark(BookmarkRequestDTO bookmarkRequestDTO);

  ResponseEntity<ResultDTO> getBookmark();
}
