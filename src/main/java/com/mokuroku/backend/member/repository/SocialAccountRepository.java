package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    // 특정 회원의 모든 소셜 계정 조회
    List<SocialAccount> findByMemberId(Long memberId);

    // 특정 소셜 제공자와 providerId로 SocialAccount 조회 (로그인 시 사용)
    Optional<SocialAccount> findByProviderAndProviderId(String provider, String providerId);
}
