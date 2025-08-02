package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TokenReissueRequestDTO {

    @NotBlank(message = "Access token은 필수입니다.")
    private String accessToken;

    @NotBlank(message = "Refresh token은 필수입니다.")
    private String refreshToken;
}

/*
✅ 사용 목적
사용자가 만료된 Access Token을 가지고 있고,
서버에 저장된 Refresh Token과 비교하여 유효하면
새로운 Access Token (필요시 Refresh Token 포함) 발급
 */