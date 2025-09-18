package com.mokuroku.backend.comment.service.impl;

import com.mokuroku.backend.comment.dto.CommentDTO;
import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.repository.CommentRepository;
import com.mokuroku.backend.comment.service.CommentService;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.MemberAuthUtil;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final MemberRepository memberRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  // 현재 대댓글 기능이 없기 때문에 그 부분은 제외하고 만듬
  @Override
  public List<CommentDTO> getComment(Long postId) {

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    if (post.getStatus() != '1') {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }

    List<Comment> comments = commentRepository.findAllByPostAndStatusOrderByRegDateDesc(
        post, CommentStatus.POSTED);

    List<CommentDTO> commentDTOs = comments.stream()
        .map(CommentDTO::toDTO)
        .toList();

    return commentDTOs;
  }

  @Override
  public CommentDTO createComment(Long postId, CommentDTO commentDTO) {

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    PostEntity post = postRepository.findByIdAndStatus(postId, '1')
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    Comment comment = CommentDTO.createComment(commentDTO, post, member);
    commentRepository.save(comment);

    CommentDTO result = CommentDTO.toDTO(comment);

    return result;
  }

  @Override
  public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    // enum 값으로 변경시 그에 맞게 수정
    PostEntity post = postRepository.findByIdAndStatus(postId, '1')
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    Comment comment = commentRepository.findByIdAndStatus(commentId, CommentStatus.POSTED)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

    if (!comment.getPost().equals(post)) {
      throw new CustomException(ErrorCode.NOT_FOUND_COMMENT);
    }

    if (!comment.getEmail().equals(member)) {
      throw new CustomException(ErrorCode.INVALID_COMMENT_OWNERSHIP);
    }

    Comment build = comment.toBuilder()
        .content(commentDTO.getContent())
        .updateDate(LocalDateTime.now())
        .build();
    commentRepository.save(build);

    CommentDTO result = CommentDTO.toDTO(build);

    return result;
  }
}
