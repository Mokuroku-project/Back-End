package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원가입 성공 후 클라이언트에게 반환되는 응답 데이터를 전달하기 위한 DTO (Data Transfer Object) 클래스.
 * 회원가입 결과로 생성된 회원 정보를 포함하며, API 응답으로 사용된다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, JSON 직렬화를 위해 Lombok 어노테이션을 활용한다.
 */
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Lombok: 매개변수 없는 기본 생성자 생성, private 접근 제어로 외부 직접 호출 방지 (Jackson 역직렬화 지원)
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 생성, 선택적으로 사용
@ToString // Lombok: 객체의 toString() 메서드 자동 생성, 디버깅 시 객체 정보 출력에 사용
@Schema(description = "회원가입 응답 DTO") // Swagger: API 문서에서 이 클래스의 설명 제공
public class RegisterResponseDTO {

    /**
     * 회원의 고유 식별자. 데이터베이스에서 회원을 식별하기 위해 사용된다.
     */
    @Schema(description = "회원 ID", example = "1", required = true) // Swagger: API 문서에 회원 ID 필드 설명, 예시, 필수 여부 명시
    private Long memberId;

    /**
     * 회원의 이메일 주소. 회원가입 시 사용된 고유 식별자로, 클라이언트에게 반환된다.
     */
    @Schema(description = "이메일", example = "user@example.com", required = true) // Swagger: API 문서에 이메일 필드 설명, 예시, 필수 여부 명시
    private String email;

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
     * 회원의 권한. 사용자가 일반 사용자인지 관리자인지 나타낸다.
     * Role 열거형을 통해 값이 제한된다 (USER, ADMIN).
     */
    @Schema(description = "사용자 권한 (예: USER, ADMIN)", example = "USER", required = true) // Swagger: API 문서에 권한 필드 설명, 예시, 필수 여부 명시
    private Role role;

    /**
     * 회원 가입일. 회원이 시스템에 등록된 날짜와 시간을 나타낸다.
     * ISO 8601 형식의 LocalDateTime 객체로 저장된다.
     */
    @Schema(description = "가입일", example = "2025-08-07T12:34:56", required = true) // Swagger: API 문서에 가입일 필드 설명, 예시, 필수 여부 명시
    private LocalDateTime regDate;

    /**
     * 회원의 권한을 정의하는 열거형.
     * USER: 일반 사용자 권한
     * ADMIN: 관리자 권한
     */
    public enum Role {
        USER, // 일반 사용자 권한, 기본적으로 부여됨
        ADMIN // 관리자 권한, 특정 사용자에게 부여됨
    }
}