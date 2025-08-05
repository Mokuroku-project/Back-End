package com.mokuroku.backend.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {
    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;
    private String role; // "admin" 또는 "user"
    private boolean status; // true: usable, false: unusable
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