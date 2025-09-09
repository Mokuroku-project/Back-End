package com.mokuroku.backend.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoDTO {

  private String name;
  private String description;
  private int price;
  private String url;
  private LocalDateTime regDate;
}
