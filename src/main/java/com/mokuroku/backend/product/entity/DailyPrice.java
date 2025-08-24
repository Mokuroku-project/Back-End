package com.mokuroku.backend.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "daily_price")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dailyPriceId;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private LocalDate date;
}
