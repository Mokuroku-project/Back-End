package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원가입 요청 데이터를 전달하기 위한 DTO (Data Transfer Object) 클래스.
 * 클라이언트로부터 받은 데이터를 서버로 전달하며, 입력값의 유효성 검증을 수행한다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, JSON 직렬화를 위해 Lombok 어노테이션을 활용한다.
 */
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Lombok: 매개변수 없는 기본 생성자 생성, private 접근 제어로 외부 직접 호출 방지 (Jackson 직렬화 지원)
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 생성, 선택적으로 사용
@Schema(description = "회원가입 요청 DTO") // Swagger: API 문서에서 이 클래스의 설명 제공
public class RegisterRequestDTO {

    /**
     * 사용자 이메일 주소. 회원가입 시 고유 식별자로 사용되며, 유효한 이메일 형식이어야 한다.
     * NotBlank: null 또는 빈 문자열("")을 허용하지 않음.
     * Email: 이메일 형식(예: user@example.com)을 준수해야 함.
     */
    @NotBlank(message = "이메일은 필수입니다.") // jakarta.validation: 이메일이 비어있거나 null이면 에러 메시지 출력
    @Email(message = "이메일 형식이 올바르지 않습니다.") // jakarta.validation: 이메일 형식 검증
    @Schema(description = "사용자 이메일", example = "user@example.com", required = true) // Swagger: API 문서에 이메일 필드 설명, 예시, 필수 여부 명시
    private String email;

    /**
     * 사용자 비밀번호. 보안 요구사항에 따라 영문자와 숫자를 포함해야 하며, 길이는 8~25자여야 한다.
     * NotBlank: null 또는 빈 문자열("")을 허용하지 않음.
     * Size: 비밀번호 길이 제한(최소 8자, 최대 25자).
     * Pattern: 정규식을 통해 영문자와 숫자만 포함하도록 제한.
     */
    @NotBlank(message = "비밀번호는 필수입니다.") // jakarta.validation: 비밀번호가 비어있거나 null이면 에러 메시지 출력
    @Size(min = 8, max = 25, message = "비밀번호는 8자 이상 25자 이하이어야 합니다.") // jakarta.validation: 비밀번호 길이 검증
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,25}$", message = "비밀번호는 영문자와 숫자만 포함해야 하며, 8~25자이어야 합니다.") // jakarta.validation: 비밀번호 형식 검증 (영문자+숫자)
    @Schema(description = "비밀번호 (영문자+숫자 포함, 8~25자)", example = "pass1234", required = true) // Swagger: API 문서에 비밀번호 필드 설명, 예시, 필수 여부 명시
    private String password;

    /**
     * 사용자 닉네임. 회원가입 시 사용자가 표시되는 이름으로 사용된다.
     * NotBlank: null 또는 빈 문자열("")을 허용하지 않음.
     */
    @NotBlank(message = "닉네임은 필수입니다.") // jakarta.validation: 닉네임이 비어있거나 null이면 에러 메시지 출력
    @Schema(description = "사용자 닉네임", example = "mokuroku", required = true) // Swagger: API 문서에 닉네임 필드 설명, 예시, 필수 여부 명시
    private String nickname;

    /**
     * 프로필 이미지 URL. 선택적으로 제공되며, 사용자의 프로필 이미지를 나타낸다.
     * 필수 입력이 아니므로 null 허용.
     */
    @Schema(description = "프로필 이미지 URL (선택)", example = "https://example.com/profile.jpg", required = false) // Swagger: API 문서에 프로필 이미지 필드 설명, 예시, 선택 여부 명시
    private String profileImage;

    /**
     * 소셜 로그인 여부를 나타내는 플래그. true일 경우 소셜 로그인, false일 경우 일반 로그인.
     * 기본값은 false로, 필수 입력이 아님.
     */
    @Schema(description = "소셜 로그인 여부 (true=소셜, false=일반)", example = "false", required = false) // Swagger: API 문서에 소셜 로그인 여부 필드 설명, 예시, 선택 여부 명시
    private boolean socialLoginCheck;
}