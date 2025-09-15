package com.mokuroku.backend.comment.service;

import com.mokuroku.backend.common.ResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

  ResponseEntity<ResultDTO> getComment(Long postId);
}
