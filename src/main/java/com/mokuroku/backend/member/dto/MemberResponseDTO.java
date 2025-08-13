package com.mokuroku.backend.member.dto;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.fasterxml.jackson.annotation.JsonValue; // JSON 직렬화 시 열거형 값을 제어하기 위한 어노테이션
import io.swagger.v3.oas.annotations.media.Schema; // Swagger 문서화를 위한 어노테이션
import lombok.*; // Lombok 어노테이션으로 보일러플레이트 코드 최소화
import java.time.LocalDateTime; // 날짜 및 시간 처리를 위한 클래스

// Getter 메서드를 자동 생성하여 필드 값을 외부에서 읽을 수 있도록 함
@Getter
// Builder 패턴을 활성화하여 객체 생성을 유연하게 처리, toBuilder=true로 기존 객체를 기반으로 수정 가능
@Builder(toBuilder = true)
// 매개변수가 없는 기본 생성자를 private으로 설정하여 Jackson 역직렬화용으로 사용
// 외부에서 직접 호출되지 않도록 제한
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// 모든 필드를 포함하는 생성자를 생성, 필요 시 명시적 사용 가능
@AllArgsConstructor
// toString 메서드를 자동 생성하여 객체의 문자열 표현 제공
@ToString
// Swagger 문서화: 이 클래스가 회원 정보 응답 DTO임을 설명
@Schema(description = "회원 정보 응답 DTO")
public class MemberResponseDTO {

    // 회원의 고유 식별자
    // @Schema : Swagger 문서에서 필드 설명, 예시, 필수 여부를 정의
    @Schema(description = "회원 ID", example = "1", required = true)
    private Long memberId;

    // 회원의 이메일 주소
    @Schema(description = "이메일", example = "user@example.com", required = true)
    private String email;

    // 회원의 닉네임
    @Schema(description = "닉네임", example = "mokuroku", required = true)
    private String nickname;

    // 회원의 프로필 이미지 URL, 선택적 필드
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", required = false)
    private String profileImage;

    // 회원의 권한 (USER 또는 ADMIN)
    @Schema(description = "회원 권한 (USER, ADMIN)", example = "USER", required = true)
    private Role role;

    // 회원 계정 상태 (true=활성, false=정지/탈퇴)
    @Schema(description = "회원 상태 (true=사용 가능, false=정지/탈퇴)", example = "true", required = true)
    private boolean status;

    // 회원 가입 일자
    @Schema(description = "가입 일자", example = "2025-08-07T12:34:56", required = true)
    private LocalDateTime regDate;

    // 회원 권한을 정의하는 열거형
    public enum Role {
        USER, // 일반 사용자 권한
        ADMIN; // 관리자 권한

        // JSON 직렬화 시 열거형 값을 문자열로 변환 (예: "USER", "ADMIN")
        // @JsonValue : Jackson이 이 메서드를 사용하여 Role 값을 직렬화
        @JsonValue
        public String getValue() {
            return name();
        }
    }
}