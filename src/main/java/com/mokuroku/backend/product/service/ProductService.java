package com.mokuroku.backend.product.service;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.CrawlingRequestDTO;
import com.mokuroku.backend.product.dto.ProductDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

  ResponseEntity<ResultDTO> wishListRegist(WishlistDTO wishListDTO);

  ResponseEntity<ResultDTO> getProductInfo(long wishlistId);

  ResponseEntity<ResultDTO> putWishlist(long wishlistId, WishlistDTO wishListDTO);

  ResponseEntity<ResultDTO> deleteWishlist(long wishlistId);

  ResponseEntity<ResultDTO> getWishlistList();

  Mono<ProductDTO> crawling(CrawlingRequestDTO crawlingRequestDTO);
}
