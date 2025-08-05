package com.mokuroku.backend.sns.service.impl;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.PostRepository;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        // 기본값 설정
        postDTO.setStatus('1'); // 활성 상태로 설정
        
        PostEntity postEntity = postDTO.toEntity(postDTO);
        postEntity.setRegDate(LocalDateTime.now());
        PostEntity savedEntity = postRepository.save(postEntity);
        
        return PostDTO.fromEntity(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDTO getPost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        
        if (postEntity.getStatus() == '0') {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        
        return PostDTO.fromEntity(postEntity);
    }
} 