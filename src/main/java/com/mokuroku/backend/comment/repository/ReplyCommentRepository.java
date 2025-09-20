package com.mokuroku.backend.comment.repository;

import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.entity.ReplyComment;
import com.mokuroku.backend.comment.type.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
    List<ReplyComment> findAllByCommentAndStatus(Comment comment, CommentStatus status);
}
