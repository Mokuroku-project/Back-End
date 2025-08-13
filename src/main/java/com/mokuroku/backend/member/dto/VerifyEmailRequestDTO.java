package com.mokuroku.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 이메일 인증 요청 데이터를 담는 DTO (Data Transfer Object) 클래스.
 * 사용자가 이메일 인증 링크를 클릭할 때 서버로 전송되는 UUID 형식의 인증 토큰을 포함한다.
 * 예: POST /verify-email 요청 시 클라이언트에서 이 DTO를 JSON으로 전송.
 * Swagger(OpenAPI)를 통해 API 문서화에 사용되며, JSON 직렬화 및 역직렬화를 위해 Lombok과 Jackson 어노테이션을 활용한다.
 * 민감 데이터인 토큰의 로깅을 피하기 위해 @ToString에서 제외된다.
 */
@Getter // Lombok: 모든 필드에 대한 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Lombok: 빌더 패턴 구현, toBuilder=true로 기존 객체를 복사해 수정 가능한 빌더 생성
@ToString(exclude = "token") // Lombok: 객체의 toString() 메서드 자동 생성, token 필드는 민감 데이터로 제외하여 로깅 방지
@Schema(description = "이메일 인증 요청 DTO") // Swagger: API 문서에서 이 클래스의 설명 제공
public class VerifyEmailRequestDTO {

    /**
     * 이메일 인증을 위한 UUID 형식의 토큰.
     * 사용자가 이메일로 받은 인증 링크에 포함된 토큰으로, 서버에서 유효성을 검증하여 이메일 인증을 완료한다.
     * NotBlank: null 또는 빈 문자열("")을 허용하지 않음.
     * Pattern: UUID 형식(8-4-4-4-12의 16진수 패턴, 예: 550e8400-e29b-41d4-a716-446655440000)을 준수해야 함.
     * Size: UUID는 고정된 36자 길이로 제한.
     */
    @NotBlank(message = "인증 토큰은 필수입니다.") // jakarta.validation: 토큰이 비어있거나 null이면 에러 메시지 출력
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "인증 토큰은 유효한 UUID 형식이어야 합니다."
    ) // jakarta.validation: UUID 형식 검증 (8-4-4-4-12 패턴의 16진수 문자열)
    @Size(min = 36, max = 36, message = "인증 토큰은 36자 UUID 형식이어야 합니다.") // jakarta.validation: 토큰 길이 고정 (36자)
    @Schema(description = "이메일 인증 링크에 포함된 UUID 형식의 인증 토큰", example = "550e8400-e29b-41d4-a716-446655440000", required = true) // Swagger: API 문서에 토큰 필드 설명, 예시, 필수 여부 명시
    private String token;

    /**
     * Jackson을 통한 JSON 역직렬화를 위한 생성자.
     * 클라이언트로부터 전송된 JSON 데이터의 "token" 필드를 매핑하여 객체를 생성한다.
     * @param token 클라이언트로부터 전송된 UUID 형식의 인증 토큰
     */
    @JsonCreator // Jackson: JSON 역직렬화 시 이 생성자를 사용하여 객체 생성
    public VerifyEmailRequestDTO(@JsonProperty("token") String token) { // Jackson: JSON의 "token" 속성을 이 파라미터에 매핑
        this.token = token;
    }
}