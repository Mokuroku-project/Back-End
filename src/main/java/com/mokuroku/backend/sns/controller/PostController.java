package com.mokuroku.backend.sns.controller;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성 페이지 (GET)
    @GetMapping("/edit")
    public ResponseEntity<ResultDTO<String>> getEditPage() {
        return ResponseEntity.ok(new ResultDTO<>("게시글 작성 페이지", "edit page"));
    }

    // 게시글 등록 (POST)
    @PostMapping
    public ResponseEntity<ResultDTO<PostDTO>> registerPost(@RequestBody PostDTO postDTO) {
        PostDTO registeredPost = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResultDTO<>("게시글이 성공적으로 등록되었습니다.", registeredPost));
    }

    // 게시글 조회 (단일)
    @GetMapping("/{postId}")
    public ResponseEntity<ResultDTO<PostDTO>> getPost(@PathVariable Long postId) {
        PostDTO post = postService.getPost(postId);
        return ResponseEntity.ok(new ResultDTO<>("게시글 조회 성공", post));
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<ResultDTO<String>> getAllPosts() {
        return ResponseEntity.ok(new ResultDTO<>("게시글 목록 조회", "posts list"));
    }
}
