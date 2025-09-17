package com.mokuroku.backend.bookmark.service;

import com.mokuroku.backend.bookmark.dto.BookmarkDTO;
import com.mokuroku.backend.bookmark.dto.BookmarkRequestDTO;
import com.mokuroku.backend.common.ResultDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkService {

  void addBookmark(BookmarkRequestDTO bookmarkRequestDTO);

  List<BookmarkDTO> getBookmark();

  void deleteBookmark(BookmarkRequestDTO bookmarkRequestDTO);
}
