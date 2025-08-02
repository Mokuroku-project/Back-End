package com.mokuroku.backend.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private Long userId;
    private String email;
    private String nickname;
    private String accessToken;  // JWT 발급 시 사용
    private String refreshToken; // 선택적
}
