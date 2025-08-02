package com.mokuroku.backend.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;  // 선택적으로 포함

    // 만약 refreshToken은 최초 로그인 때만 내려보내고 재발급 시에는 생략하고 싶다면,
    // 컨트롤러/서비스에서 조건적으로 null 세팅 가능
}