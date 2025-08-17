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

    @Override // 게시글 조회
    @Transactional(readOnly = true)
    public PostDTO getPost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        
        if (postEntity.getStatus() == '0') {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }
        
        return PostDTO.fromEntity(postEntity);
    }

    @Override // 게시글 수정
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        
        if (postEntity.getStatus() == '0') {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }
        
        // 게시글 수정 - 직접 필드 설정
        postEntity.setContent(postDTO.getContent());
        postEntity.setLocation(postDTO.getLocation());
        postEntity.setVisibility(postDTO.getVisibility());
        
        PostEntity updatedEntity = postRepository.save(postEntity);
        
        return PostDTO.fromEntity(updatedEntity);
    }

    @Override // 게시글 삭제
    public void deletePost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        
        if (postEntity.getStatus() == '0') {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }
        
        // 게시글 삭제 처리
        postEntity.delete();
        postRepository.save(postEntity);
    }
} 