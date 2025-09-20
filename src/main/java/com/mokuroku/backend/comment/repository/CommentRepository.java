package com.mokuroku.backend.comment.repository;

import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.sns.entity.PostEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPostAndStatusOrderByRegDateDesc(PostEntity post, CommentStatus commentStatus);

  Optional<Comment> findByCommentIdAndStatus(Long commentId, CommentStatus commentStatus);
}
