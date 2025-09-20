package com.mokuroku.backend.product.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.CrawlingRequestDTO;
import com.mokuroku.backend.product.dto.ProductDTO;
import com.mokuroku.backend.product.dto.ProductInfoDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import com.mokuroku.backend.product.service.ProductService;
import com.mokuroku.backend.product.service.impl.ProductServiceImpl;
import java.util.List;
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
  public ResponseEntity<ResultDTO<WishlistDTO>> wishlistRegist(@RequestBody WishlistDTO wishListDTO) {
    WishlistDTO result = productService.wishListRegist(wishListDTO);
    return ResponseEntity.ok(new ResultDTO<>("관심상품 등록에 성공했습니다.", result));
  }

  @GetMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO<ProductInfoDTO>> getProductInfo(@PathVariable Long wishlistId) {
    ProductInfoDTO result = productService.getProductInfo(wishlistId);
    return ResponseEntity.ok(new ResultDTO<>("관심상품 상세 정보가져오기에 성공했습니다.", result));
  }

  @PutMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO<WishlistDTO>> putWishlist(@PathVariable Long wishlistId,
      @RequestBody WishlistDTO wishListDTO) {
    WishlistDTO result = productService.putWishlist(wishlistId, wishListDTO);
    return ResponseEntity.ok(new ResultDTO<>("관심상품 수정에 성공했습니다.", result));
  }

  @DeleteMapping("/{wishlistId}")
  public ResponseEntity<ResultDTO> deleteWishlist(@PathVariable Long wishlistId) {
    productService.deleteWishlist(wishlistId);
    return ResponseEntity.ok(new ResultDTO<>("관심상품 삭제에 성공했습니다.", null));
  }

  @GetMapping()
  public ResponseEntity<ResultDTO<List<WishlistDTO>>> getWishlistList() {
    List<WishlistDTO> result = productService.getWishlistList();
    return ResponseEntity.ok(new ResultDTO<>("관심상품 리스트를 불러오는데 성공했습니다.", result));
  }

  @GetMapping("/crawl")
  public ResponseEntity<ResultDTO<ProductDTO>> crawl(@RequestBody CrawlingRequestDTO crawlingRequestDTO) {
    ProductDTO productInfo = productService.crawling(crawlingRequestDTO).block();
    return ResponseEntity.ok(new ResultDTO<>("크롤링 성공", productInfo));
  }

  @PostMapping("/test")
  public String test() {
    productServiceImpl.scheduledCrawling();
    return "성공";
  }
}

