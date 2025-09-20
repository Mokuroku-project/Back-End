package com.mokuroku.backend.product.dto;

import com.mokuroku.backend.product.entity.Wishlist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDTO {

  private Long wishListId;
  private String email;
  private String name;
  private String description;
  private LocalDateTime regDate;

  public static WishlistDTO toDTO(Wishlist wishlist) {
    return WishlistDTO.builder()
        .wishListId(wishlist.getWishlistId())
        .email(wishlist.getEmail().getEmail())
        .name(wishlist.getName())
        .description(wishlist.getDescription())
        .regDate(wishlist.getRegDate())
        .build();
  }
}
