package com.mokuroku.backend.product.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.CrawlingRequestDTO;
import com.mokuroku.backend.product.dto.ProductDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import com.mokuroku.backend.product.service.ProductService;
import com.mokuroku.backend.product.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;
  private final ProductServiceImpl productServiceImpl;

  @PostMapping()
  public ResponseEntity<ResultDTO> wishlistRegist(@RequestBody WishlistDTO wishListDTO) {
    ResponseEntity<ResultDTO> result = productService.wishListRegist(wishListDTO);
    return result;
  }

  @GetMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO> getProductInfo(@PathVariable Long wishlistId) {
    ResponseEntity<ResultDTO> result = productService.getProductInfo(wishlistId);
    return result;
  }

  @PutMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO> putWishlist(@PathVariable Long wishlistId,
      @RequestBody WishlistDTO wishListDTO) {
    ResponseEntity<ResultDTO> result = productService.putWishlist(wishlistId, wishListDTO);
    return result;
  }

  @DeleteMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO> deleteWishlist(@PathVariable Long wishlistId) {
    ResponseEntity<ResultDTO> result = productService.deleteWishlist(wishlistId);
    return result;
  }

  @GetMapping()
  public ResponseEntity<ResultDTO> getWishlistList() {
    ResponseEntity<ResultDTO> result = productService.getWishlistList();
    return result;
  }

  @GetMapping("/crawl")
  public ResponseEntity<ResultDTO> crawl(@RequestBody CrawlingRequestDTO crawlingRequestDTO) {
    ProductDTO productInfo = productService.crawling(crawlingRequestDTO).block();
    return ResponseEntity.ok(new ResultDTO<>("크롤링 성공", productInfo));
  }

  @PostMapping("/test")
  public String test() {
    productServiceImpl.scheduledCrawling();
    return "성공";
  }
}

