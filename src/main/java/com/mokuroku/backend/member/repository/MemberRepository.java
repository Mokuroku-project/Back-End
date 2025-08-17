package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;  // Member Entity 클래스
import org.springframework.data.jpa.repository.JpaRepository;  // Spring Data JPA
import java.util.Optional;  // null 안전성을 위한 Optional

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}

/*
◆ 인터페이스 선언
javapublic interface MemberRepository extends JpaRepository<Member, Long>
JpaRepository<Member, Long> 분석:

Member: 이 Repository가 다루는 Entity 타입
Long: Member Entity의 Primary Key 타입 (ID가 Long 타입)
*/
