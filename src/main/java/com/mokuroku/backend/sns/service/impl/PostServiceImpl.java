package com.mokuroku.backend.sns.service.impl;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.PostRepository;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MessageSource messageSource;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        log.debug("포스트 생성 시도: postDTO={}", postDTO);
        // 기본값 설정
        postDTO.setStatus('1'); // 활성 상태로 설정
        
        PostEntity postEntity = postDTO.toEntity(postDTO);
        postEntity.setRegDate(LocalDateTime.now());
        PostEntity savedEntity = postRepository.save(postEntity);

        log.info("포스트 생성 성공: postId={}", savedEntity.getPostId());
        return PostDTO.fromEntity(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDTO getPost(Long postId) {
        log.debug("포스트 조회 시도: postId={}", postId);
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND, messageSource));
        if (postEntity.getStatus() == '0') {
            log.warn("비활성 포스트: postId={}", postId);
            throw new CustomException(ErrorCode.POST_NOT_FOUND, messageSource);
        }

        log.debug("포스트 조회 성공: postId={}, status={}", postId, postEntity.getStatus());
        return PostDTO.fromEntity(postEntity);
    }
} 