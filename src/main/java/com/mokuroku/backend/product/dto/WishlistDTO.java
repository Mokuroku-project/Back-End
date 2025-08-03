package com.mokuroku.backend.product.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class WishlistDTO {

  private Long wishListId;
  private String email;
  private String name;
  private String description;
  private LocalDateTime regDate;
}
