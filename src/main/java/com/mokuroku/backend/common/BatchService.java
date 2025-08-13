package com.mokuroku.backend.common;

import com.mokuroku.backend.member.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 주기적 배치 작업을 처리하는 서비스 클래스.
 * EmailVerification의 resendCount 리셋 및 만료된 레코드 삭제.
 * 작업 결과(레코드 수)를 로깅하여 모니터링 지원.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchService {

    private final EmailVerificationRepository emailVerificationRepository;

    /**
     * 매일 자정 lastResendDate가 하루 이상 지난 레코드의 resendCount를 리셋하고,
     * 만료된 EmailVerification 레코드를 삭제.
     * 영향을 받은 레코드 수를 로깅.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanExpiredVerifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        // resendCount 리셋
        long resetCount = emailVerificationRepository.countByLastResendDateBefore(yesterday);
        emailVerificationRepository.resetResendCountBefore(yesterday);
        log.info("resendCount 리셋 완료: 기준 시간 - {}, 리셋된 레코드 수 - {}", yesterday, resetCount);

        // 만료된 레코드 삭제
        long deletedCount = emailVerificationRepository.countByExpiryTimeBefore(now);
        emailVerificationRepository.deleteByExpiryTimeBefore(now);
        log.info("만료된 EmailVerification 레코드 삭제 완료: 기준 시간 - {}, 삭제된 레코드 수 - {}", now, deletedCount);
    }
}