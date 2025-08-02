package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmailAndToken(String email, String token);

    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);
}
