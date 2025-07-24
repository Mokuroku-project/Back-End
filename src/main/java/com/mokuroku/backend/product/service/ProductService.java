package com.mokuroku.backend.product.service;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.product.dto.WishlistDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

  ResponseEntity<ResultDTO> wishListRegist(WishlistDTO wishListDTO);
}
