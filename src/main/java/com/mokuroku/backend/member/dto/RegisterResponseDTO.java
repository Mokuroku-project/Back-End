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

/*
📌 회원가입 성공 후 클라이언트에게 응답으로 보내는 DTO
보통 이메일, 닉네임, 가입일 등을 담습니다.
엔티티 자체를 반환하지 않고, 이 DTO를 사용하는 것이 보안과 캡슐화 측면에서 좋습니다.
 */