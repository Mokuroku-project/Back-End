package com.mokuroku.backend.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "daily_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DailyPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dailyPriceId;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer price;
  private LocalDateTime capturedAt;
  private LocalDate date;
}
