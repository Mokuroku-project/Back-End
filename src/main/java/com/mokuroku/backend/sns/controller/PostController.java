package com.mokuroku.backend.sns.controller;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록 (POST)
    @PostMapping
    public ResponseEntity<ResultDTO<PostDTO>> registerPost(@RequestBody PostDTO postDTO) {
        PostDTO registeredPost = postService.createPost(postDTO);
        return ResponseEntity.ok()
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

    // 게시글 수정 (PUT)
    @PutMapping("/{postId}")
    public ResponseEntity<ResultDTO<PostDTO>> updatePost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(postId, postDTO);
        return ResponseEntity.ok(new ResultDTO<>("게시글이 성공적으로 수정되었습니다.", updatedPost));
    }
}
