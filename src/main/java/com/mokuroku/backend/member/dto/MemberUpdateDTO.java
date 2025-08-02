package com.mokuroku.backend.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberUpdateDTO {

    // 닉네임 변경
    private String nickname;

    // 프로필 이미지 URL (선택사항)
    private String profileImage;

    // 현재 비밀번호 (확인을 위해 필요할 수 있음)
    private String currentPassword;

    // 새 비밀번호 (기본 규칙 적용)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,25}$", message = "영문+숫자 포함 8~25자리여야 합니다.")
    private String newPassword;
}
