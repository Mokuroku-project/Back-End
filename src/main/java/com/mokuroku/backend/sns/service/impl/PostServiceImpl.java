package com.mokuroku.backend.sns.service.impl;

import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.entity.LocationEntity;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.repository.LocationRepository;
import com.mokuroku.backend.sns.repository.PostRepository;
import com.mokuroku.backend.sns.service.LocationService;
import com.mokuroku.backend.sns.service.PostService;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        // 기본값 설정
        postDTO.setStatus('1'); // 활성 상태로 설정
        
        Member member = memberRepository.findById(postDTO.getEmail())
    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        // 위경도 기반으로 locationEntity 조회 또는 생성
        LocationEntity locationEntity = locationService.findOrCreate(
                postDTO.getLatitude(), postDTO.getLongitude()
        );

        PostEntity postEntity = new PostEntity();
        postEntity.setMember(member);
        postEntity.setContent(postDTO.getContent());
        postEntity.setVisibility(postDTO.getVisibility());
        postEntity.setRegDate(LocalDateTime.now());
        postEntity.setLocation(locationEntity);

        PostEntity savedEntity = postRepository.save(postEntity);
        
        return PostDTO.fromEntity(savedEntity, member);
    }

  @Override // 게시글 조회
  @Transactional(readOnly = true)
  public PostDTO getPost(Long postId) {
    PostEntity postEntity = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    if (postEntity.getStatus() == '0') {
      throw new CustomException(ErrorCode.NOT_FOUND_POST);
    }

    @Override // 게시글 수정
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        
        if (postEntity.getStatus() == '0') {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        LocationEntity locationEntity = locationService.findOrCreate(
                postDTO.getLatitude(), postDTO.getLongitude()
        );

        // 게시글 수정 - 직접 필드 설정
        postEntity.setContent(postDTO.getContent());
        postEntity.setLocation(locationEntity);
        postEntity.setVisibility(postDTO.getVisibility());
        
        PostEntity updatedEntity = postRepository.save(postEntity);
        
        return PostDTO.fromEntity(updatedEntity, postEntity.getMember());

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

    return PostDTO.fromEntity(updatedEntity, postEntity.getMember());
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

  @Override // 게시글 목록 조회
  @Transactional(readOnly = true)
  public List<PostDTO> getAllPosts() {
    List<PostEntity> postEntities = postRepository.findByStatusOrderByRegDateDesc('1');

    List<PostDTO> result = new ArrayList<>();
    for (PostEntity postEntity : postEntities) {
      PostDTO postDTO = PostDTO.fromEntity(postEntity, postEntity.getMember());
      result.add(postDTO);
    }
    return result;
  }

  @Override // 특정 회원 게시글 목록 조회
  @Transactional(readOnly = true)
  public List<PostDTO> getPostsByNickname(String nickname) {
    List<PostEntity> postEntities = postRepository.findByMember_NicknameAndStatusOrderByRegDateDesc(
        nickname, '1');

    List<PostDTO> result = new ArrayList<>();
    for (PostEntity postEntity : postEntities) {
      PostDTO postDTO = PostDTO.fromEntity(postEntity, postEntity.getMember());
      result.add(postDTO);
    }
    return result;
  }
}