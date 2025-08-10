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
@Schema(description = "회원 정보 DTO")
public class MemberDTO {

 @Schema(description = "회원 ID", example = "1")
 private Long id;

 @Schema(description = "회원 이메일", example = "user@example.com")
 private String email;

 @Schema(description = "비밀번호 (암호화된 상태)", example = "$2a$10$...")
 private String password;

 @Schema(description = "회원 닉네임", example = "mokuroku")
 private String nickname;

 @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
 private String profileImage;

 @Schema(description = "소셜 로그인 여부 (true=소셜 로그인)", example = "false")
 private boolean socialLogin;

 @Schema(description = "가입일시", example = "2025-08-07T12:34:56")
 private LocalDateTime regDate;

 @Schema(description = "탈퇴일시 (null이면 탈퇴하지 않음)", example = "2025-09-01T15:00:00")
 private LocalDateTime withdrawalDate;

 @Schema(description = "회원 권한 (예: user, admin)", example = "user")
 private String role;

 @Schema(description = "계정 활성 상태 (true=사용 가능, false=정지/탈퇴)", example = "true")
 private boolean status;
}

// ✅ 필드 설명 요약 (Swagger UI에서 표시됨)
// 필드	설명	예시
// id	회원 고유 식별자	1
// email	이메일 주소	user@example.com
// password	암호화된 비밀번호	$2a$10$...
// nickname	닉네임	mokuroku
// profileImage	프로필 이미지 URL	https://example.com/img.jpg
// socialLogin	소셜 로그인 여부	false
// regDate	가입 일시	2025-08-07T12:34:56
// withdrawalDate	탈퇴 일시 (null이면 미탈퇴)	2025-09-01T15:00:00
// role	사용자 권한	user, admin
// status	계정 상태 (true=활성, false=비활성)	true