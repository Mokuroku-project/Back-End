package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Member 엔티티를 관리하는 JPA 리포지토리 인터페이스.
 * 회원 정보를 조회, 저장, 삭제하며, 이메일과 닉네임 중복 확인 기능을 제공.
 * 예: 회원가입 시 이메일/닉네임 중복 체크, 로그인 시 이메일로 회원 조회.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 주어진 이메일로 회원 정보를 조회.
     * 로그인 또는 회원 정보 조회 시 사용.
     *
     * @param email 사용자 이메일 (예: user@example.com)
     * @return Optional<Member> 이메일에 해당하는 회원 레코드, 없으면 empty
     */
    Optional<Member> findByEmail(String email);

    /**
     * 주어진 이메일이 이미 존재하는지 확인.
     * 회원가입 시 이메일 중복 체크에 사용.
     *
     * @param email 사용자 이메일 (예: user@example.com)
     * @return boolean 이메일이 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 주어진 닉네임이 이미 존재하는지 확인.
     * 회원가입 또는 닉네임 변경 시 중복 체크에 사용.
     *
     * @param nickname 사용자 닉네임 (예: mokuroku)
     * @return boolean 닉네임이 존재하면 true, 없으면 false
     */
    boolean existsByNickname(String nickname);
}