package com.mokuroku.backend.comment.controller;

import com.mokuroku.backend.comment.service.CommentService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/sns/{postId}/comment")
  public ResponseEntity<ResultDTO> getComments(@PathVariable Long postId) {

    ResponseEntity<ResultDTO> result = commentService.getComment(postId);
    return result;
  }
}
