package com.mokuroku.backend.comment.service;

import com.mokuroku.backend.comment.dto.CommentDTO;
import com.mokuroku.backend.common.ResultDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

  List<CommentDTO> getComment(Long postId);

  CommentDTO createComment(Long postId, CommentDTO commentDTO);

  CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO);
}
