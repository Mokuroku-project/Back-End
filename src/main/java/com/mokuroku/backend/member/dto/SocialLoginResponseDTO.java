package com.mokuroku.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocialLoginResponseDTO {

    private String token;        // JWT 토큰
    private String nickname;     // 사용자 닉네임
    private boolean isNewUser;   // 신규 회원 여부
}
