package com.mokuroku.backend.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken; // 쿠키 전략이면 null/미사용
    private String tokenType;    // "Bearer"
    private long   expiresIn;    // 초 단위
    private String role;         // "USER" / "ADMIN"
}
