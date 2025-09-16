package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank @Pattern(regexp = "^[0-9]{6}$") private String code;
}