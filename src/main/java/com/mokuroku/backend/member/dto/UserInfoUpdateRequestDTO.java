package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoUpdateRequestDTO {

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String newEmail;  // 선택적: 이메일 변경

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,25}$",
            message = "영문+숫자 포함 8~25자리여야 합니다."
    )
    private String newPassword;
}
