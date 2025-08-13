package com.mokuroku.backend.member.repository;

import com.mokuroku.backend.member.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * EmailVerification 엔티티를 관리하는 JPA 리포지토리 인터페이스.
 * 이메일 검증 레코드를 조회, 저장, 삭제하며, 재전송 횟수 리셋 및 만료 레코드 삭제 기능을 제공.
 * 예: 이메일 인증 링크 검증, 재전송 요청 처리, 배치 작업으로 만료 레코드 정리.
 */
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    /**
     * 주어진 토큰으로 EmailVerification 레코드를 조회.
     * 인증 링크 클릭 시 토큰만으로 검증 정보를 빠르게 확인.
     *
     * @param token 고유 검증 토큰 (UUID 형식)
     * @return Optional<EmailVerification> 토큰에 해당하는 레코드, 없으면 empty
     */
    Optional<EmailVerification> findByToken(String token);

    /**
     * 주어진 이메일과 미인증 상태로 EmailVerification 레코드를 조회.
     * 재전송 요청 시 미인증 토큰 확인에 사용.
     *
     * @param email 사용자 이메일
     * @return Optional<EmailVerification> 조건에 맞는 레코드, 없으면 empty
     */
    Optional<EmailVerification> findByEmailAndVerifiedFalse(String email);

    /**
     * 주어진 이메일과 토큰이 모두 일치하는 EmailVerification 레코드를 조회.
     * 인증 링크 클릭 시 보안성 강화를 위해 사용.
     *
     * @param email 사용자 이메일
     * @param token 고유 검증 토큰 (UUID 형식)
     * @return Optional<EmailVerification> 조건에 맞는 레코드, 없으면 empty
     */
    Optional<EmailVerification> findByEmailAndToken(String email, String token);

    /**
     * 주어진 이메일에 해당하는 최신 EmailVerification 레코드를 조회.
     * 재전송 요청 시 최신 상태 확인에 사용.
     *
     * @param email 사용자 이메일
     * @return Optional<EmailVerification> 최신 레코드, 없으면 empty
     */
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    /**
     * 하루 이상 지난 lastResendDate를 가진 레코드의 resendCount를 리셋.
     * 배치 작업으로 매일 자정 실행.
     *
     * @param date 기준 시간 (예: 24시간 이전)
     * @return int 리셋된 레코드 수
     */
    @Modifying
    @Query("UPDATE EmailVerification e SET e.resendCount = 0 WHERE e.lastResendDate < :date")
    int resetResendCountBefore(LocalDateTime date);

    /**
     * 만료된 EmailVerification 레코드를 삭제.
     * 배치 작업으로 매일 자정 실행.
     *
     * @param date 기준 시간 (현재 시간 이전)
     * @return int 삭제된 레코드 수
     */
    @Modifying
    @Query("DELETE FROM EmailVerification e WHERE e.expiryTime < :date")
    int deleteByExpiryTimeBefore(LocalDateTime date);

    /**
     * lastResendDate가 주어진 시간보다 이전인 레코드 수를 계산.
     * 배치 작업의 리셋 대상 레코드 수 확인.
     *
     * @param date 기준 시간
     * @return long 리셋 대상 레코드 수
     */
    long countByLastResendDateBefore(LocalDateTime date);

    /**
     * expiryTime이 주어진 시간보다 이전인 레코드 수를 계산.
     * 배치 작업의 삭제 대상 레코드 수 확인.
     *
     * @param date 기준 시간
     * @return long 삭제 대상 레코드 수
     */
    long countByExpiryTimeBefore(LocalDateTime date);
}