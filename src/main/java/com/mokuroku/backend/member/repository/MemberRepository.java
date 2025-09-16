package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  List<Member> findByStatus(char i);

  boolean existsByNickname(String nickname);

  //토큰 아직 구현된거 아니니까 임시로 이메일 조회
  Optional<Object> findByEmail(String email);
}

