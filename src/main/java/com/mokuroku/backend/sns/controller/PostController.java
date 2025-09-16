package com.mokuroku.backend.sns.controller;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.common.ResultDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
        System.out.println("status: "+postDTO.getStatus());
        PostDTO registeredPost = postService.createPost(postDTO);
        return ResponseEntity.ok()
                .body(new ResultDTO<>("게시글 등록에 성공했습니다.", registeredPost));
    }

    // 게시글 조회 (단일)
    @GetMapping("/{postId}")
    public ResponseEntity<ResultDTO<PostDTO>> getPost(@PathVariable Long postId) {
        PostDTO post = postService.getPost(postId);
        return ResponseEntity.ok(new ResultDTO<>("게시글 조회에 성공했습니다.", post));
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<ResultDTO<List<PostDTO>>> getAllPosts() {
        List<PostDTO> postList = postService.getAllPosts();
        return ResponseEntity.ok(new ResultDTO<>("게시글 목록 조회에 성공했습니다.", postList));
    }

    // 특정 회원 게시글 목록 조회
    @GetMapping("/member/{nickname}")
    public ResponseEntity<ResultDTO<List<PostDTO>>> getPostsByNickname(@PathVariable String nickname) {
        List<PostDTO> postList = postService.getPostsByNickname(nickname);
        return ResponseEntity.ok(new ResultDTO<>("회원 게시글 목록 조회에 성공했습니다.", postList));
    }

    // 게시글 수정 (PUT)
    @PutMapping("/{postId}")
    public ResponseEntity<ResultDTO<PostDTO>> updatePost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(postId, postDTO);
        return ResponseEntity.ok(new ResultDTO<>("게시글 수정에 성공했습니다.", updatedPost));
    }

    // 게시글 삭제 (DELETE)
    @DeleteMapping("/{postId}")
    public ResponseEntity<ResultDTO<String>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ResultDTO<>("게시글 삭제에 성공했습니다.", null));
    }
}
