package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "회원가입 응답 DTO")
public class RegisterResponseDTO {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "mokuroku")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "사용자 권한 (예: user, admin)", example = "user")
    private String role;

    @Schema(description = "가입일", example = "2025-08-07T12:34:56")
    private LocalDateTime regDate;
}



// ✅ Swagger UI에서 표현될 필드 설명 요약
// 필드	설명	예시
// memberId	회원 고유 ID	1
// email	회원 이메일	user@example.com
// nickname	회원 닉네임	mokuroku
// profileImage	프로필 이미지 URL	https://example.com/...
// role	회원 권한	user, admin
// regDate	가입 일시	2025-08-07T12:34:56