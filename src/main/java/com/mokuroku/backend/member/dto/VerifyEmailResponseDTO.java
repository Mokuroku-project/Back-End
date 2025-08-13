package com.mokuroku.backend.member.dto;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.fasterxml.jackson.annotation.JsonCreator; // JSON 역직렬화를 위한 생성자 지정 어노테이션
import com.fasterxml.jackson.annotation.JsonProperty; // JSON 속성과 필드를 매핑하기 위한 어노테이션
import io.swagger.v3.oas.annotations.media.Schema; // Swagger 문서화를 위한 어노테이션
import lombok.*; // Lombok 어노테이션으로 보일러플레이트 코드 최소화

/**
 * 이메일 인증 및 재전송 결과를 담는 DTO.
 * 성공 여부와 결과를 설명하는 메시지를 포함하며, 이메일 인증/재전송 요청 처리 후 클라이언트에 반환된다.
 */
@Getter // 모든 필드에 대해 getter 메서드를 자동 생성
@Builder(toBuilder = true) // Builder 패턴을 활성화하여 객체 생성을 유연하게 처리, toBuilder=true로 기존 객체를 기반으로 수정 가능
@ToString // toString 메서드를 자동 생성하여 객체의 문자열 표현 제공
@Schema(description = "이메일 인증/재전송 응답 DTO") // Swagger 문서화: 이 클래스가 이메일 인증/재전송 응답 DTO임을 설명
public class VerifyEmailResponseDTO {

    // 이메일 인증 또는 재전송 요청의 처리 성공 여부를 나타내는 필드
    // @Schema : Swagger 문서에서 필드 설명, 예시, 필수 여부를 정의
    @Schema(description = "처리 성공 여부", example = "true", required = true)
    private final boolean success;

    // 처리 결과에 대한 설명 메시지 (성공 또는 실패 사유)
    @Schema(description = "결과 메시지 (성공 또는 실패 사유)", example = "이메일 인증 성공", required = true)
    private final String message;

    // JSON 역직렬화를 위한 생성자
    // @JsonCreator : Jackson이 이 생성자를 사용하여 JSON 데이터를 객체로 변환
    // @JsonProperty : JSON 속성 이름을 필드와 매핑
    @JsonCreator
    public VerifyEmailResponseDTO(@JsonProperty("success") boolean success, @JsonProperty("message") String message) {
        this.success = success; // 성공 여부 초기화
        this.message = message; // 메시지 초기화
    }
}