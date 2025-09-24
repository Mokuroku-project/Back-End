package com.mokuroku.backend.sns.service.impl;

import com.mokuroku.backend.member.security.MemberAuthUtil;
import com.mokuroku.backend.sns.dto.PostDTO;
import com.mokuroku.backend.sns.entity.LocationEntity;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.entity.PostStatus;
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
        postDTO.setStatus(PostStatus.ACTIVE); // 활성 상태로 설정

        Member member = memberRepository.findById(postDTO.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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

        String email = MemberAuthUtil.getLoginUserId();

        // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getStatus().equals("1")) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (postEntity.getStatus() == PostStatus.DELETED) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        return PostDTO.fromEntity(postEntity, member);
    }

    @Override // 게시글 수정
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (postEntity.getStatus() == PostStatus.DELETED) {
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
    }

    @Override // 게시글 삭제
    public void deletePost(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (postEntity.getStatus() == PostStatus.DELETED) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        // 게시글 삭제 처리
        postEntity.delete();
        postRepository.save(postEntity);
    }

    @Override // 게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findByStatusOrderByRegDateDesc(PostStatus.ACTIVE);

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
