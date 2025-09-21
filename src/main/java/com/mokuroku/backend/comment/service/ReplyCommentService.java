package com.mokuroku.backend.comment.service;

import com.mokuroku.backend.comment.dto.ReplyCommentDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReplyCommentService {

    ReplyCommentDTO creteReplyComment(Long commentId, ReplyCommentDTO replyCommentDTO);
}
