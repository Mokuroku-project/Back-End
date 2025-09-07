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
    private String refreshToken;
}