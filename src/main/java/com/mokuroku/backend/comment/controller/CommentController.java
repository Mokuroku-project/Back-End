package com.mokuroku.backend.comment.controller;

import com.mokuroku.backend.comment.dto.CommentDTO;
import com.mokuroku.backend.comment.dto.CommentListDTO;
import com.mokuroku.backend.comment.dto.ReplyCommentDTO;
import com.mokuroku.backend.comment.service.CommentService;
import com.mokuroku.backend.comment.service.ReplyCommentService;
import com.mokuroku.backend.common.ResultDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReplyCommentService replyCommentService;

    @GetMapping("/sns/{postId}/comment")
    public ResponseEntity<ResultDTO<List<CommentListDTO>>> getComments(@PathVariable Long postId) {
        List<CommentListDTO> result = commentService.getComment(postId);
        return ResponseEntity.ok(new ResultDTO<>("댓글 조회에 성공했습니다.", result));
    }

    @PostMapping("/sns/{postId}/comment")
    public ResponseEntity<ResultDTO<CommentDTO>> createComment(@PathVariable Long postId,
                                                               @RequestBody CommentDTO commentDTO) {
        CommentDTO result = commentService.createComment(postId, commentDTO);
        return ResponseEntity.ok(new ResultDTO<>("댓글 작성에 성공했습니다.", result));
    }

    @PutMapping("/sns/{postId}/comment/{commentId}")
    public ResponseEntity<ResultDTO<CommentDTO>> updateComment(@PathVariable Long postId,
                                                               @PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        CommentDTO result = commentService.updateComment(postId, commentId, commentDTO);
        return ResponseEntity.ok(new ResultDTO<>("댓글 수정에 성공했습니다.", result));
    }

    @DeleteMapping("/sns/{postId}/comment/{commentId}")
    public ResponseEntity<ResultDTO> deleteComment(@PathVariable Long postId,
                                                   @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok(new ResultDTO<>("댓글 삭제 성공했습니다.", null));
    }

    @PostMapping("/comment/{commentId}/reply")
    public ResponseEntity<ResultDTO<ReplyCommentDTO>> createReplyComment(
            @PathVariable Long commentId, @RequestBody ReplyCommentDTO replyCommentDTO) {
        ReplyCommentDTO result = replyCommentService.creteReplyComment(commentId, replyCommentDTO);
        return ResponseEntity.ok(new ResultDTO<>("대댓글이 등록에 성공했습니다.", result));
    }

    @PutMapping("/comment/{commentId}/reply/{replyCommentId}")
    public ResponseEntity<ResultDTO<ReplyCommentDTO>> updateReplyComment(
            @PathVariable Long commentId, @PathVariable Long replyCommentId,
            @RequestBody ReplyCommentDTO replyCommentDTO) {
        ReplyCommentDTO result = replyCommentService.updateReplyComment(commentId, replyCommentId, replyCommentDTO);
        return ResponseEntity.ok(new ResultDTO<>("대댓글 수정에 성공했습니다.", result));
    }
}
