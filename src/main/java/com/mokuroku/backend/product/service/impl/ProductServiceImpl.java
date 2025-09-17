package com.mokuroku.backend.product.service.impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.notification.event.PriceChangedEvent;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
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
  private final TransactionTemplate tx;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public WishlistDTO wishListRegist(WishlistDTO wishListDTO) {

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

    return wishlistDTO;
  }

  @Override
  public ProductInfoDTO getProductInfo(long wishlistId) {

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

    return builder.build();
  }

  @Override
  public WishlistDTO putWishlist(long wishlistId, WishlistDTO wishListDTO) {

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

    return wishlistDTO;
  }

  @Override
  public void deleteWishlist(long wishlistId) {

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

    // 해당 위시리스트의 상품정보도 삭제
    productRepository.findByWishlist(wishlist).ifPresent(productRepository::delete);
  }

  @Override
  public List<WishlistDTO> getWishlistList() {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원 검증
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    List<Wishlist> wishlists = wishlistRepository.findByEmail(member);
    List<WishlistDTO> wishlistDTOList = wishlists.stream()
        .map(WishlistDTO::toDTO)
        .toList();

    return wishlistDTOList;
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
                  if (product.getNationCode() == null) {
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
  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  public void scheduledCrawling() {
    List<Wishlist> wishlists = wishlistRepository.findAllByMemberStatusWithProducts("1");

    crawlMultipleProducts(wishlists)
        .filter(pair -> {
          ProductDTO productDTO = pair.getSecond();
          return productDTO.getName() != null && !productDTO.getName().isEmpty() && productDTO.getPrice() > 0;
        })
        .flatMap(pair ->
                Mono.fromCallable(() ->
                    tx.execute(status -> updateProduct(pair.getFirst(), pair.getSecond()))
                ).subscribeOn(Schedulers.boundedElastic()),
            CRAWL_CONCURRENCY
        )
        .doOnComplete(() -> log.info("scheduledCrawling completed. Total wishlists processed: {}", wishlists.size()))
        .onErrorContinue((e, o) -> log.error("scheduledCrawling error: {}", e.getMessage(), e))
        .subscribe();
  }

  public Product updateProduct(Wishlist wishlist, ProductDTO productDTO) {

    int newPrice = productDTO.getPrice();

    Product product;
    // DB에서 존재 여부 확인
    Optional<Product> existingProductOpt = productRepository.findByWishlist(wishlist);

    if (existingProductOpt.isPresent()) {
      Product existing = existingProductOpt.get();
      int oldPrice = existing.getPrice();

      // 가격 변동 없으면 저장 생략
      if (newPrice > 0 && newPrice != existing.getPrice()) {
        product = productRepository.save(ProductDTO.updateEntity(productDTO, existing));

        // 가격 변동시 이벤트 발생
        eventPublisher.publishEvent(
            new PriceChangedEvent(
                wishlist.getEmail().getEmail(),
                product.getProductId(),
                product.getName(),
                oldPrice,
                newPrice
            )
        );
      } else {
        product = existing;
      }
    } else {
      product = productRepository.save(ProductDTO.toNewEntity(productDTO, wishlist));
    }

    LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime capturedUtc = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

    // DailyPrice 저장
    dailyPriceRepository.findByProductAndDate(product, date).ifPresentOrElse(dp -> {
      DailyPrice updateDp = dp.toBuilder()
          .price(newPrice)
          .capturedAt(capturedUtc)
          .build();

      dailyPriceRepository.save(updateDp);
    }, () -> {
      DailyPrice newDp = DailyPrice.builder()
          .product(product)
          .price(newPrice)
          .capturedAt(capturedUtc)
          .date(date)
          .build();

      dailyPriceRepository.save(newDp);
    });

    return product;
  }
}
