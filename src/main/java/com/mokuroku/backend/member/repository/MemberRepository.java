package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;  // Member Entity 클래스
import org.springframework.data.jpa.repository.JpaRepository;  // Spring Data JPA
import java.util.Optional;  // null 안전성을 위한 Optional

public interface MemberRepository extends JpaRepository<Member, String> {

}

