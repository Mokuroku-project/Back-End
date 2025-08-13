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
    private String role;

    @Schema(description = "회원 상태 (true = 사용 가능, false = 정지/탈퇴)", example = "true")
    private boolean status;

    @Schema(description = "가입 일자", example = "2025-08-07T12:34:56")
    private LocalDateTime regDate;
}

/*
    3. MemberResponseDTO
    ⚠️ 상황에 따라 필요
    이 DTO는 일반적으로 "회원 정보 조회"와 같은 조회용 응답 DTO로 사용됩니다.
    예를 들어 마이페이지, 프로필 보기, 회원 목록 등에서 활용될 수 있습니다.
    그러나 회원가입 전까지는 보통 사용되지 않습니다.
    ✅ 나중에 이메일 인증 이후 사용자 정보 응답이 필요해지면 사용하는 것이 좋습니다.
 */

// ✅ Swagger 문서에 표시될 필드 요약
// 필드	설명	예시
// memberId	회원 고유 ID	1
// email	회원 이메일	user@example.com
// nickname	회원 닉네임	mokuroku
// profileImage	프로필 이미지 URL	https://example.com/img.png
// role	권한 (예: 일반 사용자, 관리자)	user, admin
// status	계정 활성 여부	true
// regDate	가입 일시	2025-08-07T12:34:56