package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,25}$", message = "영문자와 숫자를 포함해 8~25자여야 합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    private String profileImage;  // null 가능

    private boolean socialLoginCheck; // true = 소셜 로그인

}

/*
📌 회원가입 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO
예: 이메일, 비밀번호, 닉네임, 소셜 여부, 프로필 이미지 등
서비스 계층에서 이 데이터를 바탕으로 Member 엔티티를 생성합니다.
 */