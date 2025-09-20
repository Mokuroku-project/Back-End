
package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailOnlyDTO {
    @Email
    @NotBlank
    private String email;
}

