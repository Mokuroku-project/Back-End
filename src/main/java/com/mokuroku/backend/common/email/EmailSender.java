package com.mokuroku.backend.common.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender javaMailSender;

    /**
     * 단순 텍스트 이메일 전송
     *
     * @param to 받는 사람 이메일
     * @param subject 제목
     * @param text 본문 내용
     */
    public void send(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);

            log.info("✅ 이메일 전송 완료: to={}, subject={}", to, subject);
        } catch (Exception e) {
            log.error("❌ 이메일 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이메일 전송 실패");
        }
    }
}

/*
📌 이 구조의 장점
공통 모듈화: 이메일 전송 로직을 중복 없이 한 곳에 관리
테스트 편의성: 나중에 모킹 가능
유지보수 용이: 템플릿 메일, HTML 메일 등 확장 용이
 */