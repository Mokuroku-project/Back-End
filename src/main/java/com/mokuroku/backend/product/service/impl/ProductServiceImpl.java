package com.mokuroku.backend.product.service.impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.MemberAuthUtil;
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

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    Wishlist wishlist = Wishlist.builder()
        .member(member)
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

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    // Wishlist 조회
    Wishlist wishlist = wishlistRepository.findByWishlistIdAndMember(wishlistId, member)
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

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    Wishlist wishlist = wishlistRepository.findById(wishlistId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WISHLIST));

    // 해당 회원의 위시리스트가 맞는지 확인
    if (!wishlist.getMember().equals(member)) {
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

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    Wishlist wishlist = wishlistRepository.findById(wishlistId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WISHLIST));

    // 해당 회원의 위시리스트가 맞는지 확인
    if (!wishlist.getMember().equals(member)) {
      throw new CustomException(ErrorCode.NOT_FOUND_WISHLIST);
    }

    wishlistRepository.delete(wishlist);

    // 해당 위시리스트의 상품정보도 삭제
    productRepository.findByWishlist(wishlist).ifPresent(productRepository::delete);
  }

  @Override
  public List<WishlistDTO> getWishlistList() {

    String email = MemberAuthUtil.getLoginUserId();

    // 회원인지 검증 -> 회원상태 enum 값으로 변경되면 그 상태에 맞게 수정
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!member.getStatus().equals("1")) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    List<Wishlist> wishlists = wishlistRepository.findByMember(member);
    List<WishlistDTO> wishlistDTOList = wishlists.stream()
        .map(WishlistDTO::toDTO)
        .toList();

    return wishlistDTOList;
  }

  // 1) 내부 공용: 인증 불필요, 핵심 크롤링만 수행
  private Mono<ProductDTO> crawlCore(String nationCode, String keyword) {
    String baseUrl = "http://localhost:5000";
    String nation = nationCode.toLowerCase();

    if (!nation.equals("kr") && !nation.equals("jp")) {
      return Mono.error(new CustomException(ErrorCode.INVALID_NATION_CODE));
    }

    return webClientBuilder.baseUrl(baseUrl)
        .build()
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/crawl")
            .queryParam("nationCode", nation)
            .queryParam("keyword", keyword)
            .build())
        .retrieve()
        .bodyToMono(CrawlingResponseDTO.class)
        .map(wrapper -> {
          if (wrapper.getData() != null && !wrapper.getData().isEmpty()) {
            ProductDTO product = wrapper.getData().get(0);
            if (product.getNationCode() == null) product.setNationCode(nation);
            return product;
          }
          return new ProductDTO();
        })
        .timeout(Duration.ofSeconds(60))
        .onErrorMap(e -> new CustomException(ErrorCode.CRAWLING_UNKNOWN_ERROR));
  }

  // 2) 사용자 호출용: 여기서만 로그인/회원상태 검증
  @Override
  public Mono<ProductDTO> crawling(CrawlingRequestDTO req) {
    String email = MemberAuthUtil.getLoginUserId();
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    if (!"1".equals(member.getStatus())) {
      throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
    }

    return crawlCore(req.getNationCode(), req.getName());
  }

  // 3) 스케줄러/배치용: 회원 검증 없이 core 호출
  public Flux<Pair<Wishlist, ProductDTO>> crawlMultipleProducts(List<Wishlist> wishlists) {
    return Flux.fromIterable(wishlists)
        .flatMap(wl ->
                crawlCore(wl.getNationCode(), wl.getName())
                    .map(dto -> Pair.of(wl, dto))
                    .onErrorContinue((e, o) ->
                        log.error("Crawling error for {}: {}", wl.getName(), e.getMessage(), e)
                    ),
            CRAWL_CONCURRENCY
        );
  }

  // 자정마다 실행 (cron: 초 분 시 일 월 요일)
  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  public void scheduledCrawling() {
    List<Wishlist> wishlists = wishlistRepository.findAllByMemberStatusWithProducts("1");

    crawlMultipleProducts(wishlists)
        .filter(pair -> {
          ProductDTO productDTO = pair.getSecond();
          return productDTO.getName() != null && !productDTO.getName().isEmpty()
              && productDTO.getPrice() > 0;
        })
        .flatMap(pair ->
                Mono.fromCallable(() ->
                    tx.execute(status -> updateProduct(pair.getFirst(), pair.getSecond()))
                ).subscribeOn(Schedulers.boundedElastic()),
            CRAWL_CONCURRENCY
        )
        .doOnComplete(() -> log.info("scheduledCrawling completed. Total wishlists processed: {}",
            wishlists.size()))
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
                wishlist.getMember().getEmail(),
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
