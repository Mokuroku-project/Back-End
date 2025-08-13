package com.mokuroku.backend.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 정보를 저장하는 엔티티 클래스.
 * 데이터베이스의 'member' 테이블에 매핑되며, 회원가입, 로그인, 계정 관리 등 회원 관련 정보를 관리한다.
 * 이메일 검증, 소셜 로그인, 계정 상태 등을 포함하여 다양한 회원 상태를 추적한다.
 * 예: 회원가입 후 이메일 인증, 로그인, 계정 상태 변경, 탈퇴 처리 등.
 * JPA를 통해 데이터베이스와 매핑되며, JSON 직렬화/역직렬화를 위해 Jackson 어노테이션을 활용한다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, Lombok으로 보일러플레이트 코드를 최소화한다.
 */
@Entity // JPA: 이 클래스를 데이터베이스 테이블에 매핑되는 엔티티로 지정
@Table(name = "member", indexes = {
        @Index(name = "idx_email", columnList = "email"), // JPA: email 컬럼에 인덱스 생성, 검색 성능 최적화
        @Index(name = "idx_nickname", columnList = "nickname") // JPA: nickname 컬럼에 인덱스 생성, 검색 성능 최적화
})
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED) // Lombok: 매개변수 없는 기본 생성자 생성, protected 접근 제어로 JPA 요구사항 충족 및 외부 직접 호출 방지
@ToString(exclude = {"password", "profileImage"}) // Lombok: 객체의 toString() 메서드 자동 생성, 민감 데이터(password, profileImage) 제외하여 로깅 방지
@Schema(description = "회원 엔티티") // Swagger: API 문서에서 이 엔티티의 설명 제공
public class Member {

    /**
     * 회원의 고유 식별자. 데이터베이스에서 자동 증가하는 기본 키(PK)로 사용된다.
     */
    @Id // JPA: 이 필드를 엔티티의 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: 기본 키를 자동 증가 방식으로 생성
    @Schema(description = "회원 고유 식별자", example = "1", required = true) // Swagger: API 문서에 ID 필드 설명, 예시, 필수 여부 명시
    private Long id;

    /**
     * 회원의 이메일 주소. 회원 식별을 위한 고유 키로 사용되며, 필수 입력이다.
     * 최대 100자 길이, 유효한 이메일 형식이어야 하며, 고유 제약 조건이 적용된다.
     * 예: user@example.com
     */
    @Email(message = "유효한 이메일 형식이어야 합니다.") // jakarta.validation: 이메일 형식 검증
    @Column(nullable = false, unique = true, length = 100) // JPA: null 불가, 고유 제약 조건, 최대 100자
    @Schema(description = "회원 이메일 주소", example = "user@example.com", required = true) // Swagger: API 문서에 이메일 필드 설명, 예시, 필수 여부 명시
    private String email;

    /**
     * 회원의 비밀번호. 암호화된 형태(예: BCrypt)로 저장되며, 보안상 민감 데이터로 취급된다.
     * 소셜 로그인 사용자는 비밀번호가 필요 없으므로 null을 허용한다.
     * 최대 255자 길이로 설정.
     * 예: "$2a$10$..."
     */
    @Column(length = 255) // JPA: null 허용 (소셜 로그인 고려), 최대 255자
    @Schema(description = "암호화된 비밀번호 (소셜 로그인 시 null 가능)", example = "$2a$10$...", required = false) // Swagger: API 문서에 비밀번호 필드 설명, 예시, 선택 여부 명시
    private String password;

    /**
     * 회원의 닉네임. 사용자 표시 이름으로 사용되며, 고유해야 하고 필수 입력이다.
     * 최대 50자 길이로 제한.
     * 예: mokuroku
     */
    @Column(nullable = false, unique = true, length = 50) // JPA: null 불가, 고유 제약 조건, 최대 50자
    @Schema(description = "회원 닉네임", example = "mokuroku", required = true) // Swagger: API 문서에 닉네임 필드 설명, 예시, 필수 여부 명시
    private String nickname;

    /**
     * 회원의 프로필 이미지 URL. 선택 입력이며, 유효한 URL 형식이어야 한다.
     * 최대 512자 길이로 제한, null 허용.
     * 예: https://example.com/profile.jpg
     */
    @Pattern(
            regexp = "^(https?://).+$",
            message = "프로필 이미지는 유효한 URL 형식이어야 합니다."
    ) // jakarta.validation: URL 형식 검증 (http:// 또는 https://로 시작)
    @Column(length = 512) // JPA: null 허용, 최대 512자
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", required = false) // Swagger: API 문서에 프로필 이미지 필드 설명, 예시, 선택 여부 명시
    private String profileImage;

    /**
     * 소셜 로그인 여부. true일 경우 소셜 로그인, false일 경우 일반 로그인.
     * 데이터베이스에서 TINYINT(1)로 저장되며, 기본값은 false(일반 로그인).
     * 예: true (소셜 로그인), false (일반 로그인)
     */
    @Column(nullable = false) // JPA: null 불가
    @Builder.Default // Lombok: 빌더 사용 시 기본값 false로 설정
    @Schema(description = "소셜 로그인 여부 (true=소셜, false=일반)", example = "false", required = true) // Swagger: API 문서에 소셜 로그인 여부 필드 설명, 예시, 필수 여부 명시
    private boolean socialLogin = false;

    /**
     * 이메일 검증 여부. true일 경우 이메일 인증 완료, false일 경우 미인증.
     * 회원가입 후 이메일 인증 프로세스 완료 시 true로 변경된다.
     * 기본값은 false.
     */
    @Column(nullable = false) // JPA: null 불가
    @Builder.Default // Lombok: 빌더 사용 시 기본값 false로 설정
    @Schema(description = "이메일 검증 여부", example = "false", required = true) // Swagger: API 문서에 이메일 검증 여부 필드 설명, 예시, 필수 여부 명시
    private boolean emailVerified = false;

    /**
     * 회원 가입 일시. 계정 생성 시 자동으로 현재 시간으로 설정된다.
     * ISO 8601 형식의 LocalDateTime 객체로 저장된다.
     * 예: 2025-08-13T14:20:00
     */
    @Column(name = "reg_date", nullable = false) // JPA: null 불가, reg_date 컬럼에 매핑
    @Schema(description = "회원 가입 일시", example = "2025-08-13T14:20:00", required = true) // Swagger: API 문서에 가입 일시 필드 설명, 예시, 필수 여부 명시
    private LocalDateTime regDate;

    /**
     * 회원 탈퇴 일시. 회원이 계정을 탈퇴할 때 설정되며, 기본적으로 null이다.
     * 배치 작업으로 주기적으로 정리(삭제)하는 것이 권장된다.
     * 예: 2025-08-20T10:00:00
     */
    @Column(name = "withdrawal_date") // JPA: null 허용, withdrawal_date 컬럼에 매핑
    @Schema(description = "회원 탈퇴 일시 (null=활성 상태)", example = "2025-08-20T10:00:00", required = false) // Swagger: API 문서에 탈퇴 일시 필드 설명, 예시, 선택 여부 명시
    private LocalDateTime withdrawalDate;

    /**
     * 계정 상태. ACTIVE는 사용 가능, INACTIVE는 정지 또는 탈퇴 상태를 나타낸다.
     * 열거형(Status)을 사용하여 가독성과 타입 안정성을 제공한다.
     * 기본값은 ACTIVE.
     */
    @Enumerated(EnumType.STRING) // JPA: 열거형을 문자열로 저장 (예: "ACTIVE", "INACTIVE")
    @Column(nullable = false, length = 20) // JPA: null 불가, 최대 20자
    @Builder.Default // Lombok: 빌더 사용 시 기본값 ACTIVE로 설정
    @Schema(description = "계정 상태 (ACTIVE=사용 가능, INACTIVE=정지/탈퇴)", example = "ACTIVE", required = true) // Swagger: API 문서에 계정 상태 필드 설명, 예시, 필수 여부 명시
    private Status status = Status.ACTIVE;

    /**
     * 회원의 권한. USER는 일반 사용자, ADMIN은 관리자를 나타낸다.
     * 열거형(Role)을 사용하여 가독성과 타입 안정성을 제공한다.
     * 기본값은 USER.
     */
    @Enumerated(EnumType.STRING) // JPA: 열거형을 문자열로 저장 (예: "USER", "ADMIN")
    @Column(nullable = false, length = 20) // JPA: null 불가, 최대 20자
    @Builder.Default // Lombok: 빌더 사용 시 기본값 USER로 설정
    @Schema(description = "회원 권한 (USER=일반 사용자, ADMIN=관리자)", example = "USER", required = true) // Swagger: API 문서에 권한 필드 설명, 예시, 필수 여부 명시
    private Role role = Role.USER;

    /**
     * 계정 상태를 정의하는 열거형.
     * ACTIVE: 계정이 사용 가능한 상태.
     * INACTIVE: 계정이 정지되거나 탈퇴된 상태.
     */
    public enum Status {
        ACTIVE, // 사용 가능한 계정 상태
        INACTIVE // 정지 또는 탈퇴된 계정 상태
    }

    /**
     * 회원의 권한을 정의하는 열거형.
     * USER: 일반 사용자 권한, 기본적으로 부여됨.
     * ADMIN: 관리자 권한, 특정 사용자에게 부여됨.
     */
    public enum Role {
        USER, // 일반 사용자 권한
        ADMIN // 관리자 권한
    }

    /**
     * Jackson을 통한 JSON 역직렬화를 위한 생성자.
     * 클라이언트로부터 전송된 JSON 데이터를 각 필드에 매핑하여 객체를 생성한다.
     * @param id 회원 고유 식별자
     * @param email 회원 이메일 주소
     * @param password 암호화된 비밀번호
     * @param nickname 회원 닉네임
     * @param profileImage 프로필 이미지 URL
     * @param socialLogin 소셜 로그인 여부
     * @param emailVerified 이메일 검증 여부
     * @param regDate 회원 가입 일시
     * @param withdrawalDate 회원 탈퇴 일시
     * @param status 계정 상태
     * @param role 회원 권한
     */
    @JsonCreator // Jackson: JSON 역직렬화 시 이 생성자를 사용하여 객체 생성
    public Member(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("profileImage") String profileImage,
            @JsonProperty("socialLogin") boolean socialLogin,
            @JsonProperty("emailVerified") boolean emailVerified,
            @JsonProperty("regDate") LocalDateTime regDate,
            @JsonProperty("withdrawalDate") LocalDateTime withdrawalDate,
            @JsonProperty("status") Status status,
            @JsonProperty("role") Role role) { // Jackson: JSON 속성을 각 파라미터에 매핑
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.socialLogin = socialLogin;
        this.emailVerified = emailVerified;
        this.regDate = regDate;
        this.withdrawalDate = withdrawalDate;
        this.status = status;
        this.role = role;
    }

    /**
     * 엔티티 저장 전 실행되는 메서드.
     * JPA 생명주기 이벤트(@PrePersist)를 활용하여 필수 필드의 기본값을 설정한다.
     * - regDate: 레코드 생성 시간을 현재 시간으로 설정 (null일 경우).
     * - status: 기본값 ACTIVE로 설정 (null일 경우).
     * - role: 기본값 USER로 설정 (null일 경우).
     * - emailVerified: 소셜 로그인이 아닌 경우 기본값 false 유지.
     */
    @PrePersist // JPA: 엔티티 저장 전 호출
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.regDate == null) {
            this.regDate = now;
        }
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (!this.socialLogin) {
            this.emailVerified = false;
        }
    }

    /**
     * 엔티티 업데이트 전 실행되는 메서드.
     * JPA 생명주기 이벤트(@PreUpdate)를 활용하여 상태 동기화를 처리한다.
     * - withdrawalDate가 설정되면 status를 INACTIVE로 변경 (status가 INACTIVE가 아닌 경우).
     */
    @PreUpdate // JPA: 엔티티 업데이트 전 호출
    public void preUpdate() {
        if (this.withdrawalDate != null && this.status != Status.INACTIVE) {
            this.status = Status.INACTIVE;
        }
    }

    /**
     * 배치 작업을 위한 주석:
     * - withdrawalDate가 설정된 레코드나 status=INACTIVE인 레코드는 주기적으로 삭제 처리.
     * - 예: @Scheduled(cron = "0 0 0 * * ?")를 사용하여 매일 자정에 실행.
     * - 삭제 조건: withdrawalDate가 30일 지난 레코드.
     * - 쿼리 예시: DELETE FROM member WHERE withdrawal_date < :date AND status = 'INACTIVE'
     */
}