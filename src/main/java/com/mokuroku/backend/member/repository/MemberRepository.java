package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

  List<Member> findByStatus(char i);
}

