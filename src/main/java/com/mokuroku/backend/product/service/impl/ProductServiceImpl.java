package com.mokuroku.backend.product.service.impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.product.dto.CrawlingRequestDTO;
import com.mokuroku.backend.product.dto.CrawlingResponseDTO;
import com.mokuroku.backend.product.dto.ProductDTO;
import com.mokuroku.backend.product.dto.ProductInfoDTO;
import com.mokuroku.backend.product.dto.ProductInfoDTO.ProductInfoDTOBuilder;
import com.mokuroku.backend.product.dto.WishlistDTO;
import com.mokuroku.backend.product.entity.DailyPrice;
import com.mokuroku.backend.product.entity.Product;
import com.mokuroku.backend.product.entity.Wishlist;
import com.mokuroku.backend.product.repository.DailyPriceRepository;
import com.mokuroku.backend.product.repository.ProductRepository;
import com.mokuroku.backend.product.repository.WishlistRepository;
import com.mokuroku.backend.product.service.ProductService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private static final int CRAWL_CONCURRENCY = 3; // Python MAX_WORKERS와 동일하게

  private final MemberRepository memberRepository;
  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final DailyPriceRepository dailyPriceRepository;
  private final Builder webClientBuilder;

  @Override
  public ResponseEntity<ResultDTO> wishListRegist(WishlistDTO wishListDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원인지 검증 -> 회원상태에 대한 검증 추가 필요함
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Wishlist wishlist = Wishlist.builder()
        .email(member)
        .name(wishListDTO.getName())
        .description(wishListDTO.getDescription())
        .regDate(LocalDateTime.now())
        .build();

    wishlistRepository.save(wishlist);

    WishlistDTO wishlistDTO = WishlistDTO.toDTO(wishlist);

    return ResponseEntity.ok(new ResultDTO<>("관심상품 등록에 성공했습니다.", wishlistDTO));
  }

  @Override
  public ResponseEntity<ResultDTO> getProductInfo(long wishlistId) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    // Wishlist 조회
    Wishlist wishlist = wishlistRepository.findByWishlistIdAndEmail(wishlistId, member)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WISHLIST));

    ProductInfoDTOBuilder builder = ProductInfoDTO.builder()
        .name(wishlist.getName())
        .description(wishlist.getDescription());

    productRepository.findByWishlist(wishlist).ifPresent(product -> {
      builder.price(product.getPrice())
          .url(product.getUrl())
          .regDate(product.getRegDate());
    });

    return ResponseEntity.ok(new ResultDTO<>("관심상품 상세 정보가져오기에 성공했습니다.", builder.build()));
  }

  @Override
  public ResponseEntity<ResultDTO> putWishlist(long wishlistId, WishlistDTO wishListDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Wishlist wishlist = wishlistRepository.findById(wishlistId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WISHLIST));

    // 해당 회원의 위시리스트가 맞는지 확인
    if (!wishlist.getEmail().equals(member)) {
      throw new CustomException(ErrorCode.NOT_FOUND_WISHLIST);
    }

    Wishlist updateWishlist = wishlist.toBuilder()
        .name(wishListDTO.getName())
        .description(wishListDTO.getDescription())
        .build();

    wishlistRepository.save(updateWishlist);

    WishlistDTO wishlistDTO = WishlistDTO.toDTO(wishlist);

    return ResponseEntity.ok(new ResultDTO<>("관심상품 수정에 성공했습니다.", wishlistDTO));
  }

  @Override
  public ResponseEntity<ResultDTO> deleteWishlist(long wishlistId) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Wishlist wishlist = wishlistRepository.findById(wishlistId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WISHLIST));

    // 해당 회원의 위시리스트가 맞는지 확인
    if (!wishlist.getEmail().equals(member)) {
      throw new CustomException(ErrorCode.NOT_FOUND_WISHLIST);
    }

    wishlistRepository.delete(wishlist);

    return ResponseEntity.ok(new ResultDTO<>("관심상품 삭제에 성공했습니다.", null));
  }

  @Override
  public ResponseEntity<ResultDTO> getWishlistList() {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    List<Wishlist> wishlists = wishlistRepository.findByEmail(member);
    List<WishlistDTO> wishlistDTOList = wishlists.stream()
        .map(WishlistDTO::toDTO)
        .toList();

    return ResponseEntity.ok(new ResultDTO<>("관심상품 리스트를 불러오는데 성공했습니다.", wishlistDTOList));
  }

  @Override
  public Mono<ProductDTO> crawling(CrawlingRequestDTO crawlingRequestDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원인지 검증 -> 회원상태에 대한 검증 추가 필요함
    return Mono.just(email)
        .flatMap(id -> {
          Optional<Member> member = memberRepository.findById(email);
          if (member.isPresent()) {
            return Mono.just(member.get());
          } else {
            return Mono.error(new CustomException(ErrorCode.NOT_FOUND_MEMBER));
          }
        })
        .flatMap(member -> {
          String keyword = crawlingRequestDTO.getName();
          String nationCode = crawlingRequestDTO.getNationCode().toLowerCase();

          if (!nationCode.equals("kr") && !nationCode.equals("jp")) {
            return Mono.error(new CustomException(ErrorCode.INVALID_NATION_CODE));
          }

          String baseUrl = "http://localhost:5000";

          Mono<ProductDTO> productInfo = webClientBuilder.baseUrl(baseUrl)
              .build()
              .get()
              .uri(uriBuilder -> uriBuilder
                  .path("/api/crawl")
                  .queryParam("nationCode", nationCode)
                  .queryParam("keyword", keyword)
                  .build())
              .retrieve()
              .bodyToMono(CrawlingResponseDTO.class)  // Wrapper DTO
              .map(wrapper -> {
                if (wrapper.getData() != null && !wrapper.getData().isEmpty()) {
                  ProductDTO product = wrapper.getData().get(0);

                  // nationCode가 null일 수도 있으므로, 크롤링 서버에서 채워지지 않았으면 기본값 설정
                  if (!(product.getNationCode() != null)) {
                    product.setNationCode(nationCode);
                  }

                  return product;
                } else {
                  return new ProductDTO();
                }
              })
              .timeout(Duration.ofSeconds(60))
              .onErrorMap(e -> new CustomException(ErrorCode.CRAWLING_UNKNOWN_ERROR));

          return productInfo;
        });
  }

  public Flux<Pair<Wishlist, ProductDTO>> crawlMultipleProducts(List<Wishlist> wishlists) {

    return Flux.fromIterable(wishlists)
        .flatMap(wishlist -> {
          // Wishlist → CrawlingDTO 변환
          CrawlingRequestDTO dto = new CrawlingRequestDTO();
          dto.setNationCode(wishlist.getNationCode());
          dto.setName(wishlist.getName());

          return crawling(dto)
              .map(productInfo -> Pair.of(wishlist, productInfo))
              .onErrorContinue((e, o) -> log.error("Crawling error for {}: {}", wishlist.getName(),
                  e.getMessage(), e));
        }, CRAWL_CONCURRENCY);
  }

  // 자정마다 실행 (cron: 초 분 시 일 월 요일)
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void scheduledCrawling() {
    // 전체 회원의 활성 위시리스트 가져오기
    List<Wishlist> wishlists = wishlistRepository.findAllByMemberStatusWithProducts('1');

    crawlMultipleProducts(wishlists)
        .filter(pair -> {
          ProductDTO productDTO = pair.getSecond();
          return productDTO.getName() != null && !productDTO.getName().isEmpty();
        })
        .flatMap(pair -> {
          Wishlist wishlist = pair.getFirst();
          ProductDTO productDTO = pair.getSecond();

          // DB에서 존재 여부 확인
          Optional<Product> existingProductOpt = productRepository.findByWishlist(wishlist);

          if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            int oldPrice = existingProduct.getPrice();
            int newPrice = productDTO.getPrice();

            // 가격이 변경된 경우만 업데이트
            if (newPrice > 0 && newPrice != oldPrice) {
              return Mono.fromCallable(() -> {
                Product updated = ProductDTO.updateEntity(productDTO, existingProduct);
                Product saved = productRepository.save(updated);

                dailyPriceRepository.save(DailyPrice.builder()
                    .product(saved)
                    .date(LocalDate.now())
                    .build());
                return saved;
              }).subscribeOn(Schedulers.boundedElastic());
            } else {
              return Mono.empty();
            }
          } else {
            // 새 상품이면 바로 저장
            return Mono.fromCallable(() -> {
              Product newProduct = ProductDTO.toNewEntity(productDTO, wishlist);
              Product saved = productRepository.save(newProduct);

              dailyPriceRepository.save(DailyPrice.builder()
                  .product(saved)
                  .date(LocalDate.now())
                  .build());
              return saved;
            }).subscribeOn(Schedulers.boundedElastic());
          }
        }, CRAWL_CONCURRENCY)
        .doOnComplete(() -> log.info("scheduledCrawling completed. Total wishlists processed: {}",
            wishlists.size()))
        .onErrorContinue((e, o) -> log.error("scheduledCrawling error: {}", e.getMessage(), e))
        .subscribe();
  }
}
