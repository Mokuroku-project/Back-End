package com.mokuroku.backend.product.service;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.CrawlingRequestDTO;
import com.mokuroku.backend.product.dto.ProductDTO;
import com.mokuroku.backend.product.dto.ProductInfoDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

  WishlistDTO wishListRegist(WishlistDTO wishListDTO);

  ProductInfoDTO getProductInfo(long wishlistId);

  WishlistDTO putWishlist(long wishlistId, WishlistDTO wishListDTO);

  void deleteWishlist(long wishlistId);

  List<WishlistDTO> getWishlistList();

  Mono<ProductDTO> crawling(CrawlingRequestDTO crawlingRequestDTO);
}
