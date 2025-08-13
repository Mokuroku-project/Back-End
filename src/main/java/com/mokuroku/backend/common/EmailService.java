package com.mokuroku.backend.common;

import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 이메일 전송을 담당하는 공통 서비스 클래스.
 * 다국어 지원을 위해 Locale과 MessageSource를 사용하여 제목 처리.
 * 비동기 에러는 Redis에 저장, 성공 시 에러 키 삭제.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String EMAIL_SEND_ERROR_KEY = "email:send:error:";
    private static final long ERROR_CACHE_TTL_MINUTES = 60;
    private static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[ | -]?[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 인증 이메일을 비동기적으로 전송.
     * MessageSource를 사용하여 다국어 제목 처리.
     * 에러 발생 시 Redis에 에러 메시지 저장, 성공 시 에러 키 삭제.
     * @param email 수신자 이메일 (예: "user@example.com")
     * @param token 인증 토큰 (예: "550e8400-e29b-41d4-a716-446655440000")
     * @param locale 언어 설정 (예: Locale.KOREAN, Locale.ENGLISH)
     * @throws CustomException 이메일 전송 실패 시 (ErrorCode.EMAIL_SEND_FAILED)
     */
    @Async
    public void sendVerificationEmail(@Email String email, String token, Locale locale) {
        // 입력값 검증
        if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            log.warn("유효하지 않은 이메일: {}", email);
            throw new CustomException(ErrorCode.INVALID_EMAIL, messageSource);
        }
        if (token == null || !Pattern.matches(UUID_PATTERN, token)) {
            log.warn("유효하지 않은 토큰: {}", token);
            throw new CustomException(ErrorCode.INVALID_TOKEN, messageSource);
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(messageSource.getMessage("email.subject", null, locale));
            helper.setFrom(fromEmail);

            // 간단한 HTML 내용 생성
            String verificationUrl = "http://localhost:8080/api/members/verify-email?token=" + token;
            String content = "<!DOCTYPE html>" +
                    "<html><body>" +
                    "<p>" + messageSource.getMessage("email.body", null, locale) + "</p>" +
                    "<a href=\"" + verificationUrl + "\">" +
                    messageSource.getMessage("email.link.text", null, locale) + "</a>" +
                    "</body></html>";
            helper.setText(content, true);

            mailSender.send(message);
            log.info("인증 이메일 전송 성공: 이메일={}, 언어={}", email, locale.getLanguage());

            // 성공 시 Redis 에러 키 삭제
            try {
                redisTemplate.delete(EMAIL_SEND_ERROR_KEY + email);
                log.debug("Redis 에러 키 삭제 성공: {}", EMAIL_SEND_ERROR_KEY + email);
            } catch (Exception e) {
                log.warn("Redis 에러 키 삭제 실패: 키={}, 오류={}", EMAIL_SEND_ERROR_KEY + email, e.getMessage(), e);
            }
        } catch (MessagingException e) {
            String errorMessage = messageSource.getMessage("error.email_send_failed", null, locale);
            log.error("인증 이메일 전송 실패: 이메일={}, 언어={}, 오류={}", email, locale.getLanguage(), errorMessage, e);
            try {
                redisTemplate.opsForValue().set(EMAIL_SEND_ERROR_KEY + email, errorMessage, ERROR_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.debug("Redis 에러 저장 성공: {}", EMAIL_SEND_ERROR_KEY + email);
            } catch (Exception redisEx) {
                log.warn("Redis 에러 저장 실패: 키={}, 오류={}", EMAIL_SEND_ERROR_KEY + email, redisEx.getMessage(), redisEx);
            }
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED, messageSource);
        }
    }
}