package com.mokuroku.backend.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterResponseDTO {

    private Long memberId;           // 회원 ID
    private String email;            // 이메일
    private String nickname;         // 닉네임
    private String profileImage;     // 프로필 이미지
    private String role;             // 사용자 권한 (예: "user")
    private LocalDateTime regDate;   // 가입일
}
