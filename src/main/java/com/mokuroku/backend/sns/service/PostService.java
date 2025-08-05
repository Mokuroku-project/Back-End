package com.mokuroku.backend.sns.service;

import com.mokuroku.backend.sns.dto.PostDTO;

public interface PostService {

    // 게시물 작성
    PostDTO createPost(PostDTO postDTO);

    // 게시물 조회 (단일)
    PostDTO getPost(Long postId);
}
