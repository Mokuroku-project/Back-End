package com.mokuroku.backend.comment.service.impl;

import com.mokuroku.backend.comment.dto.ReplyCommentDTO;
import com.mokuroku.backend.comment.entity.Comment;
import com.mokuroku.backend.comment.entity.ReplyComment;
import com.mokuroku.backend.comment.repository.CommentRepository;
import com.mokuroku.backend.comment.repository.ReplyCommentRepository;
import com.mokuroku.backend.comment.service.ReplyCommentService;
import com.mokuroku.backend.comment.type.CommentStatus;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.MemberAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReplyCommentServiceImpl implements ReplyCommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;

    @Override
    public ReplyCommentDTO creteReplyComment(Long commentId, ReplyCommentDTO replyCommentDTO) {

        String email = MemberAuthUtil.getLoginUserId();
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getStatus().equals("1")) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }

        Comment comment = commentRepository.findByCommentIdAndStatus(commentId, CommentStatus.POSTED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        ReplyComment replyComment = ReplyCommentDTO.createReplyComment(replyCommentDTO, comment, member);
        replyCommentRepository.save(replyComment);

        ReplyCommentDTO result = ReplyCommentDTO.toDTO(replyComment);
        return result;
    }

    @Override
    public ReplyCommentDTO updateReplyComment(Long commentId, Long replyCommentId,
                                              ReplyCommentDTO replyCommentDTO) {

        String email = MemberAuthUtil.getLoginUserId();
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getStatus().equals("1")) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }

        Comment comment = commentRepository.findByCommentIdAndStatus(commentId, CommentStatus.POSTED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

        ReplyComment replyComment = replyCommentRepository.findById(replyCommentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REPLY_COMMENT));


        Long ci = replyComment.getComment().getCommentId() != null ? replyComment.getComment().getCommentId() : null;
        if (!Objects.equals(ci, comment.getCommentId())) {
            throw new CustomException(ErrorCode.INVALID_REPLY_COMMENT_PARENT_RELATION);
        }

        String mi = replyComment.getMember().getEmail() != null ? replyComment.getMember().getEmail() : null;
        if (!Objects.equals(mi, member.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_REPLY_COMMENT_OWNERSHIP);
        }

        ReplyComment updateReplyComment = replyComment.toBuilder()
                .content(replyCommentDTO.getContent())
                .updateDate(LocalDateTime.now())
                .build();
        replyCommentRepository.save(updateReplyComment);

        ReplyCommentDTO result = ReplyCommentDTO.toDTO(updateReplyComment);

        return result;
    }
}
