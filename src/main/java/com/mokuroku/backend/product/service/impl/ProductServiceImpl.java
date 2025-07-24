package com.mokuroku.backend.product.service.impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.product.dto.WishlistDTO;
import com.mokuroku.backend.product.entity.Wishlist;
import com.mokuroku.backend.product.repository.WishlistRepository;
import com.mokuroku.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final MemberRepository memberRepository;
  private final WishlistRepository wishlistRepository;

  @Override
  public ResponseEntity<ResultDTO> wishListRegist(WishlistDTO wishListDTO) {
    // 임시 테스트 이메일
    String email = "test@gmail.com";
    // 회원인지 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Wishlist wishlist = Wishlist.builder()
        .email(member)
        .name(wishListDTO.getName())
        .description(wishListDTO.getDescription())
        .build();

    wishlistRepository.save(wishlist);

    return ResponseEntity.ok(new ResultDTO<>("관심상품 등록에 성공했습니다.", wishlist));
  }
}
