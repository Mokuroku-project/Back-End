package com.mokuroku.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 정보를 전달하기 위한 DTO (Data Transfer Object) 클래스.
 * 회원의 상세 정보를 포함하며, 요청 및 응답 데이터로 사용된다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, JSON 직렬화를 위해 Lombok과 Jackson 어노테이션을 활용한다.
 * 주로 회원 조회, 업데이트, 응답 시 사용된다.
 */
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Lombok: 매개변수 없는 기본 생성자 생성, private 접근 제어로 외부 직접 호출 방지 (Jackson 역직렬화 지원)
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 생성, 선택적으로 사용
@ToString // Lombok: 객체의 toString() 메서드 자동 생성, 디버깅 시 객체 정보 출력에 사용
@Schema(description = "회원 정보 DTO") // Swagger: API 문서에서 이 클래스의 설명 제공
public class MemberDTO {

    /**
     * 회원의 고유 식별자. 데이터베이스에서 회원을 식별하기 위해 사용된다.
     */
    @Schema(description = "회원 ID", example = "1", required = true) // Swagger: API 문서에 회원 ID 필드 설명, 예시, 필수 여부 명시
    private Long id;

    /**
     * 회원의 이메일 주소. 회원가입 시 사용된 고유 식별자로, 클라이언트에게 반환된다.
     */
    @Schema(description = "이메일", example = "user@example.com", required = true) // Swagger: API 문서에 이메일 필드 설명, 예시, 필수 여부 명시
    private String email;

    /**
     * 회원의 비밀번호. 내부 매핑 및 인증에 사용되며, 보안상 응답 DTO에서는 제외하는 것을 권장한다.
     * 클라이언트로 반환되지 않도록 Jackson의 @JsonIgnore를 추가하거나, 별도의 DTO를 사용하는 것이 좋다.
     */
    @Schema(description = "비밀번호 (내부 매핑용, 응답에서는 제외)", example = "pass1234") // Swagger: API 문서에 비밀번호 필드 설명, 예시, 응답 제외 권장 명시
    private String password; // 응답 DTO에서는 제거 고려

    /**
     * 회원의 닉네임. 회원가입 시 설정된 사용자의 표시 이름으로, 클라이언트에게 반환된다.
     */
    @Schema(description = "닉네임", example = "mokuroku", required = true) // Swagger: API 문서에 닉네임 필드 설명, 예시, 필수 여부 명시
    private String nickname;

    /**
     * 회원의 프로필 이미지 URL. 선택적으로 설정된 경우에만 반환되며, null일 수 있다.
     */
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", required = false) // Swagger: API 문서에 프로필 이미지 필드 설명, 예시, 선택 여부 명시
    private String profileImage;

    /**
     * 소셜 로그인 여부를 나타내는 플래그. true일 경우 소셜 로그인, false일 경우 일반 로그인.
     */
    @Schema(description = "소셜 로그인 여부 (true=소셜, false=일반)", example = "false", required = true) // Swagger: API 문서에 소셜 로그인 여부 필드 설명, 예시, 필수 여부 명시
    private boolean socialLogin;

    /**
     * 회원 가입일. 회원이 시스템에 등록된 날짜와 시간을 나타낸다.
     * ISO 8601 형식의 LocalDateTime 객체로 저장된다.
     */
    @Schema(description = "가입일", example = "2025-08-07T12:34:56", required = true) // Swagger: API 문서에 가입일 필드 설명, 예시, 필수 여부 명시
    private LocalDateTime regDate;

    /**
     * 회원 탈퇴일. 회원이 계정을 비활성화한 날짜와 시간을 나타내며, null일 경우 계정이 활성 상태임을 의미한다.
     */
    @Schema(description = "탈퇴일 (null=활성 상태)", example = "2025-08-10T15:00:00", required = false) // Swagger: API 문서에 탈퇴일 필드 설명, 예시, 선택 여부 명시
    private LocalDateTime withdrawalDate;

    /**
     * 회원의 권한. 사용자가 일반 사용자인지 관리자인지 나타낸다.
     * Role 열거형을 통해 값이 제한된다 (USER, ADMIN).
     */
    @Schema(description = "사용자 권한 (USER, ADMIN)", example = "USER", required = true) // Swagger: API 문서에 권한 필드 설명, 예시, 필수 여부 명시
    private Role role;

    /**
     * 계정 상태. 계정이 사용 가능한지 여부를 나타낸다 (true=활성, false=비활성).
     * 탈퇴 여부나 계정 잠금 상태를 반영할 수 있다.
     */
    @Schema(description = "계정 상태 (true=사용 가능, false=사용 불가)", example = "true", required = true) // Swagger: API 문서에 계정 상태 필드 설명, 예시, 필수 여부 명시
    private boolean status;

    /**
     * 회원의 권한을 정의하는 열거형.
     * JSON 직렬화 시 열거형 값을 문자열로 반환하도록 설정되어 있다.
     */
    public enum Role {
        USER, // 일반 사용자 권한, 기본적으로 부여됨
        ADMIN; // 관리자 권한, 특정 사용자에게 부여됨

        /**
         * Jackson 직렬화 시 열거형 값을 문자열로 반환.
         * 예: USER -> "USER", ADMIN -> "ADMIN"
         * @return 열거형의 이름 문자열
         */
        @JsonValue // Jackson: 열거형 값을 JSON으로 직렬화할 때 name() 값을 사용
        public String getValue() {
            return name();
        }
    }
}