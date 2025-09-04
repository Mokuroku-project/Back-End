package com.mokuroku.backend.sns.repository;

import com.mokuroku.backend.sns.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // 게시글을 등록일 기준 내림차순으로 조회
    List<PostEntity> findByStatusOrderByRegDateDesc(char status);
    
    // 특정 회원의 활성 게시글을 등록일 기준 내림차순으로 조회
    List<PostEntity> findByMember_NicknameAndStatusOrderByRegDateDesc(String nickname, char status);
}
