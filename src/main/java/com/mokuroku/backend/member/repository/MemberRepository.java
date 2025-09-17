package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  List<Member> findByStatus(char i);

  // @Modifying + flushAutomatically 로 즉시 DB 반영, clearAutomatically 로 1차 캐시도 비워 재조회 시 최신값 보장.
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Member m set m.status = '1' where m.email = :email")
  int activateByEmail(@Param("email") String email);
  
  // 닉네임 중복 체크
  boolean existsByNickname(String nickname);
}

