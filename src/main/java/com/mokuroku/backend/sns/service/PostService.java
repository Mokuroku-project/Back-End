package com.mokuroku.backend.sns.service;

import java.util.List;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.entity.LocationEntity;

import javax.xml.stream.Location;

public interface PostService {

    // 게시물 작성
    PostDTO createPost(PostDTO postDTO);

    // 게시물 조회 (단일)
    PostDTO getPost(Long postId);

    // 게시물 목록 조회
    List<PostDTO> getAllPosts();
    
    // 특정 회원 게시물 목록 조회
    List<PostDTO> getPostsByNickname(String nickname);
    
    // 게시물 수정
    PostDTO updatePost(Long postId, PostDTO postDTO);

    // 게시물 삭제
    void deletePost(Long postId);

}
