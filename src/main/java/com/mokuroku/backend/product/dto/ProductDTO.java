package com.mokuroku.backend.product.dto;

import com.mokuroku.backend.product.entity.Product;
import com.mokuroku.backend.product.entity.Wishlist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

  private String name;
  private int price;
  private String url;
  private Double rating;
  private String nationCode;


  public static Product toNewEntity(ProductDTO dto, Wishlist wishlist) {
    return Product.builder()
        .wishlist(wishlist)
        .name(dto.getName())
        .price(dto.getPrice())
        .url(dto.getUrl())
        .nationCode(dto.nationCode)
        .build();
  }

  public static Product updateEntity(ProductDTO dto, Product product) {
    product.setName(dto.getName());
    product.setPrice(dto.getPrice());
    product.setUrl(dto.getUrl());
    product.setNationCode(dto.getNationCode());
    return product;
  }
}
