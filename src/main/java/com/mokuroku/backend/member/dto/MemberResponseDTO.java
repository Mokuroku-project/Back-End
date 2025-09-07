package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 정보 응답 DTO")
public class MemberResponseDTO {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "mokuroku")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "회원 권한 (예: user, admin)", example = "user")
    private String role; // String 유지, Enum에서 변환

    @Schema(description = "회원 상태 (usable, unusable)", example = "usable")
    private String status; // String 유지, Enum에서 변환

    @Schema(description = "가입 일자", example = "2025-08-07T12:34:56")
    private LocalDateTime regDate;
}