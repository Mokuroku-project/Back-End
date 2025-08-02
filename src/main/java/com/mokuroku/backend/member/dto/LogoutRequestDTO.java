package com.mokuroku.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDTO {
    private String accessToken;   // 클라이언트에서 전달받은 JWT
    private String refreshToken;  // 선택적
}
