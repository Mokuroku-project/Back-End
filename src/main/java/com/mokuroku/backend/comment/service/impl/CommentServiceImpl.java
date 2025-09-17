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
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.PostRepository;
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

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원인지 검증 -> 회원상태에 대한 검증 추가 필요함
    memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원인지 검증 -> 회원상태에 대한 검증 추가 필요함
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    if (post.getStatus() != '1') {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }

    Comment comment = CommentDTO.createComment(commentDTO, post, member);
    commentRepository.save(comment);

    CommentDTO result = CommentDTO.toDTO(comment);

    return result;
  }
}
