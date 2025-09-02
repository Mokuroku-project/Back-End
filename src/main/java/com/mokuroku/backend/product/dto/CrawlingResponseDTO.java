package com.mokuroku.backend.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrawlingResponseDTO {
  private String message;
  private List<ProductDTO> data;
}