package com.mokuroku.backend.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 이메일 검증 정보를 저장하는 엔티티 클래스.
 * 회원가입 시 사용자가 입력한 이메일에 대한 인증 토큰과 상태를 관리하며,
 * 데이터베이스의 'email_verification' 테이블에 매핑된다.
 * 예: 회원가입 후 이메일 인증 링크 발송 및 검증 프로세스에서 사용.
 * JPA를 통해 데이터베이스와 매핑되며, JSON 직렬화/역직렬화를 위해 Jackson 어노테이션을 활용한다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, Lombok으로 보일러플레이트 코드를 최소화한다.
 */
@Slf4j
@Entity
@Table(name = "email_verification", indexes = {
        @Index(name = "idx_email", columnList = "email"), // JPA: email 컬럼에 인덱스 생성, 검색 성능 최적화
        @Index(name = "idx_token", columnList = "token") // JPA: token 컬럼에 인덱스 생성, 검색 성능 최적화
})
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "token") // token 필드는 민감 데이터로 로깅 제외
@Schema(description = "이메일 검증 엔티티")
public class EmailVerification {

    /**
     * 인증 토큰의 만료 시간 상수 (단위: 분).
     * 기본값은 10분으로 설정되며, 인증 링크/토큰의 유효 기간을 정의한다.
     */
    private static final long TOKEN_EXPIRY_MINUTES = 10;

    /**
     * 고유 식별자. 데이터베이스에서 자동 증가하는 기본 키(PK).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "고유 식별자", example = "1", required = true)
    private Long id;

    /**
     * 검증 대상 이메일 주소.
     * 필수 입력, 최대 100자, 유효한 이메일 형식, 고유해야 함.
     */
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    @Column(nullable = false, length = 100, unique = true)
    @Schema(description = "검증 대상 이메일 주소", example = "user@example.com", required = true)
    private String email;

    /**
     * 이메일 검증용 고유 토큰.
     * UUID 형식(8-4-4-4-12의 16진수 패턴), 최대 64자, 고유해야 함.
     */
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "토큰은 유효한 UUID 형식이어야 합니다."
    )
    @Column(nullable = false, length = 64, unique = true)
    @Schema(description = "이메일 검증용 UUID 토큰", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    private String token;

    /**
     * 토큰의 만료 시간.
     * 생성 시점에서 TOKEN_EXPIRY_MINUTES(10분) 후로 설정.
     */
    @Column(name = "expiry_time", nullable = false)
    @Schema(description = "토큰 만료 시간", example = "2025-08-13T14:30:00", required = true)
    private LocalDateTime expiryTime;

    /**
     * 이메일 검증 완료 여부.
     * 기본값: false(미인증), 인증 성공 시 true.
     */
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "이메일 검증 완료 여부", example = "false", required = true)
    private boolean verified = false;

    /**
     * 인증 이메일 재전송 횟수.
     * 최대 5회 제한, 기본값 0.
     */
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "인증 이메일 재전송 횟수", example = "0", required = true)
    private int resendCount = 0;

    /**
     * 마지막 인증 이메일 재전송 시간.
     * 재전송 횟수 리셋 여부 판단에 사용, null 허용.
     */
    @Column(name = "last_resend_date")
    @Schema(description = "마지막 재전송 시간", example = "2025-08-13T14:20:00", required = false)
    private LocalDateTime lastResendDate;

    /**
     * 레코드 생성 시간.
     * 엔티티 저장 시 자동으로 현재 시간으로 설정.
     */
    @Column(name = "created_at", nullable = false)
    @Schema(description = "레코드 생성 시간", example = "2025-08-13T14:20:00", required = true)
    private LocalDateTime createdAt;

    /**
     * Jackson을 통한 JSON 역직렬화를 위한 생성자.
     */
    @JsonCreator
    public EmailVerification(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("token") String token,
            @JsonProperty("expiryTime") LocalDateTime expiryTime,
            @JsonProperty("verified") boolean verified,
            @JsonProperty("resendCount") int resendCount,
            @JsonProperty("lastResendDate") LocalDateTime lastResendDate,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.expiryTime = expiryTime;
        this.verified = verified;
        this.resendCount = resendCount;
        this.lastResendDate = lastResendDate;
        this.createdAt = createdAt;
    }

    /**
     * 엔티티 저장 전 실행.
     * createdAt과 expiryTime을 자동 설정.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.expiryTime == null) {
            this.expiryTime = now.plusMinutes(TOKEN_EXPIRY_MINUTES);
        }
    }

    /**
     * 엔티티 업데이트 전 실행.
     * resendCount 증가 시 lastResendDate를 자동 설정.
     */
    @PreUpdate
    public void preUpdate() {
        if (resendCount > 0 && lastResendDate == null) {
            this.lastResendDate = LocalDateTime.now();
            log.debug("PreUpdate: lastResendDate updated for email={}, resendCount={}", email, resendCount);
        }
    }
}