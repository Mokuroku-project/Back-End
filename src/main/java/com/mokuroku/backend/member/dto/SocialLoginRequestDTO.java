package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequestDTO {

    @NotBlank(message = "소셜 로그인 제공자(provider)는 필수입니다.")
    private String provider;  // 예: "google", "naver", "kakao"

    @NotBlank(message = "소셜 액세스 토큰은 필수입니다.")
    private String accessToken;
}
