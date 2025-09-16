package com.mokuroku.backend.notification.listener;

import com.mokuroku.backend.notification.event.PriceChangedEvent;
import com.mokuroku.backend.notification.service.PushSender;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PriceChangeListener {

  private static final Logger log = LoggerFactory.getLogger(PriceChangeListener.class);
  private final PushSender pushSender;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onPriceChange(PriceChangedEvent e) {
    try {
      Map<String,String> data = new HashMap<>();
      data.put("productId", String.valueOf(e.productId()));
      data.put("link", "/products/" + e.productId());

      pushSender.sendToEmail(
          e.email(),
          "가격 변동: " + e.productName(),
          String.format("%,d원 → %,d원", e.oldPrice(), e.newPrice()),
          data
      );
    } catch (Exception ex) {
      // 로깅만 (알림 실패가 비즈니스 트랜잭션 실패가 되지 않도록)
      log.warn("price-drop push failed: {}", ex.getMessage(), ex);
    }
  }
}

