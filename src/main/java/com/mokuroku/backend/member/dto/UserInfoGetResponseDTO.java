package com.mokuroku.backend.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoGetResponseDTO {

    private Long memberId;        // 회원 ID
    private String email;         // 이메일
    private String nickname;      // 닉네임
    private String profileImage;  // 프로필 이미지 (URL 또는 경로)
}
