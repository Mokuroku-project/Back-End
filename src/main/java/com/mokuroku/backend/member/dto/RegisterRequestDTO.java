package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class RegisterRequestDTO {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(description = "사용자 이메일", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 25, message = "비밀번호는 8자 이상 25자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "비밀번호 (영문자, 숫자, 특수문자 포함, 8~25자)", example = "pass123!@#", required = true)
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 50, message = "닉네임은 50자를 초과할 수 없습니다.")
    @Schema(description = "사용자 닉네임", example = "mokuroku", required = true)
    private String nickname;

    @Schema(description = "소셜 로그인 여부 (true=소셜, false=일반)", example = "false")
    private boolean socialLoginCheck;
}