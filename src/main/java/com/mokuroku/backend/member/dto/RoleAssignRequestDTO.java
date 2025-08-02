package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAssignRequestDTO {

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "역할 ID는 필수입니다.")
    private Long roleId;
}
