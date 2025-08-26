package com.mokuroku.backend.product.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.CrawlingDTO;
import com.mokuroku.backend.product.dto.ProductInfoDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import com.mokuroku.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<ResultDTO> wishlistRegist(@RequestBody WishlistDTO wishListDTO) {
    ResponseEntity<ResultDTO> result = productService.wishListRegist(wishListDTO);
    return result;
  }

  @GetMapping("/crawl")
  public ResponseEntity<ResultDTO> crawl(@RequestBody CrawlingDTO crawlingDTO) {
    ProductInfoDTO productInfo = productService.crawling(crawlingDTO).block();
    return ResponseEntity.ok(new ResultDTO<>("크롤링 성공", productInfo));
  }
}
