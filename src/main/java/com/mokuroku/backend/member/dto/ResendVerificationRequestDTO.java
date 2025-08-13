package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 인증 이메일 재전송 요청 데이터를 담는 DTO (Data Transfer Object) 클래스.
 * 클라이언트가 인증 이메일을 재전송 요청할 때 사용되며, 사용자의 이메일 주소를 포함한다.
 * 서버에서 입력된 이메일의 유효성을 검증하고, 인증 이메일 재전송 프로세스를 처리한다.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, JSON 직렬화를 위해 Lombok 어노테이션을 활용한다.
 */
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Lombok: 매개변수 없는 기본 생성자 생성, private 접근 제어로 외부 직접 호출 방지 (Jackson 역직렬화 지원)
@AllArgsConstructor // Lombok: 모든 필드를 포함한 생성자 생성, 선택적으로 사용
@Schema(description = "인증 이메일 재전송 요청 DTO") // Swagger: API 문서에서 이 클래스의 설명 제공
public class ResendVerificationRequestDTO {

    /**
     * 인증 이메일 재전송을 요청하는 사용자의 이메일 주소.
     * 서버에서 해당 이메일로 인증 링크를 재전송하며, 유효한 이메일 형식이어야 한다.
     * NotBlank: null 또는 빈 문자열("")을 허용하지 않음.
     * Email: 유효한 이메일 형식(예: user@example.com)을 준수해야 함.
     * Size: 이메일 길이가 최대 255자를 초과하지 않아야 함.
     */
    @NotBlank(message = "이메일은 필수입니다.") // jakarta.validation: 이메일이 비어있거나 null이면 에러 메시지 출력
    @Email(message = "유효한 이메일 형식이어야 합니다.") // jakarta.validation: 이메일 형식 검증
    @Size(max = 255, message = "이메일은 255자를 초과할 수 없습니다.") // jakarta.validation: 이메일 길이 제한 (최대 255자)
    @Schema(description = "사용자 이메일", example = "user@example.com", required = true) // Swagger: API 문서에 이메일 필드 설명, 예시, 필수 여부 명시
    private String email;
}