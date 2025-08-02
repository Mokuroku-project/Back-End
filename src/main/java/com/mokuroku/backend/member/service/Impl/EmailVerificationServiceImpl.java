package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.member.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    private static final long EXPIRATION_MINUTES = 5;

    @Override
    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        log.info("🔐 생성된 인증 코드: [{}] for email [{}]", code, email);

        // Redis 저장 (key: email, value: code, 만료 시간: 5분)
        redisTemplate.opsForValue().set(getRedisKey(email), code, EXPIRATION_MINUTES, TimeUnit.MINUTES);

        // 메일 구성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Mokuroku] 이메일 인증 코드");
        message.setText("인증 코드: " + code + "\n5분 이내에 입력해 주세요.");
        mailSender.send(message);

        log.info("📧 이메일 전송 완료: {}", email);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String redisKey = getRedisKey(email);
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(redisKey); // 인증 완료 후 삭제
            log.info("✅ 인증 성공: {}", email);
            return true;
        }

        log.warn("❌ 인증 실패: {} (입력: {}, 저장: {})", email, code, storedCode);
        return false;
    }

    private String getRedisKey(String email) {
        return "email:verification:" + email;
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999_999));
    }
}


/*
✅ 핵심 기능 요약
인증 코드를 생성한다.
JavaMailSender를 사용해 메일을 발송한다.
인증 코드를 Redis 등 임시 저장소에 저장한다.
인증 코드 검증 로직을 수행한다.

✅ 구성 요소 설명
요소	설명
JavaMailSender	Spring의 메일 전송 기능
StringRedisTemplate	인증 코드 저장 및 만료 관리
인증코드 생성	6자리 랜덤 숫자
Redis 키 구성	"email_verification:<email>"

✅ 주의사항
application.yml에 메일 설정 필요
Redis가 동작 중이어야 함 (docker-compose 또는 로컬 설치 등)
*/