package com.mokuroku.backend.notification.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import com.mokuroku.backend.notification.repository.MemberPushTokenRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushSender {

  private final FirebaseMessaging firebaseMessaging;
  private final MemberPushTokenRepository pushTokenRepository;

  // 단일 유저 이메일 기준으로 data-only 전송
  public void sendToEmail(String email, String title, String body, Map<String, String> data) {
    List<String> tokens = pushTokenRepository.findActiveTokensByEmail(email);
    if (tokens == null || tokens.isEmpty()) {
      log.info("sendToEmail: no active tokens for {}", email);
      return;
    }
    sendToTokens(tokens, title, body, data);
  }

  // 여러 토큰에 data-only 전송 + 실패 토큰 revoke
  private void sendToTokens(List<String> tokens, String title, String body, Map<String, String> data) {
    var msg = MulticastMessage.builder()
        .putAllData(commonData(title, body, data)) // SW(onBackgroundMessage)용 data-only
        .addAllTokens(tokens)
        .build();

    try {
      BatchResponse resp = firebaseMessaging.sendEachForMulticast(msg);

      // 실패 토큰 정리
      for (int i = 0; i < resp.getResponses().size(); i++) {
        SendResponse r = resp.getResponses().get(i);
        if (!r.isSuccessful()) {
          FirebaseMessagingException fme = r.getException(); // 실패 시 대부분 이 타입
          if (fme != null) {
            MessagingErrorCode code = fme.getMessagingErrorCode();
            String badToken = tokens.get(i);
            if (code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT) {
              pushTokenRepository.revokeByTokenNow(badToken);
              log.info("revoked bad token: {} ({})", badToken, code);
            } else {
              log.warn("send failure: token={}, code={}, message={}", badToken, code, fme.getMessage());
            }
          } else {
            log.warn("send failure without exception object for token={}", tokens.get(i));
          }
        }
      }

      log.info("push multicast: requested={}, success={}, failure={}",
          tokens.size(), resp.getSuccessCount(), resp.getFailureCount());

    } catch (FirebaseMessagingException e) {
      // 시스템/네트워크 레벨 장애: 업무 트랜잭션에 영향 주지 않도록 로그만
      log.error("FCM multicast call failed: {}", e.getMessage(), e);
    } catch (Exception e) {
      log.error("Unexpected push error", e);
    }
  }

  private Map<String, String> commonData(String title, String body, Map<String, String> data) {
    Map<String, String> m = new HashMap<>();
    if (data != null) m.putAll(data);
    m.putIfAbsent("title", title != null ? title : "Mokuroku");
    m.putIfAbsent("body",  body  != null ? body  : "");
    m.putIfAbsent("link",  "/push-test.html");
    return m;
  }
}

