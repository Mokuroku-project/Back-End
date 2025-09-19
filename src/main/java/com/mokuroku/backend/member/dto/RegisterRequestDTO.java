package com.mokuroku.backend.member.dto;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.entity.Member.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

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
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).*$", message = "비밀번호는 영문자와 숫자를 모두 포함해야 합니다.")
    @Schema(description = "비밀번호 (영문자+숫자 포함, 8~25자)", example = "pass1234", required = true)
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Schema(description = "사용자 닉네임", example = "mokuroku", required = true)
    private String nickname;

    public static Member joinMember(RegisterRequestDTO requestDTO, String imageUrl) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        return Member.builder()
            .email(requestDTO.getEmail())
            .password(encodedPassword)
            .nickname(requestDTO.getNickname())
            .profileImage(imageUrl)
            .socialLoginCheck("0")
            .regDate(LocalDateTime.now())
            .status("2")
            .role(Role.USER)
            .build();
    }
}

// ✅ 결과 (Swagger 문서에서 보이게 되는 설명)
// 필드	설명	예시
// email	사용자 이메일	user@example.com
// password	영문자와 숫자를 포함한 비밀번호 (8~25자)	pass1234
// nickname	사용자 닉네임	mokuroku
// profileImage	프로필 이미지 URL (선택 사항)	https://example.com/...
// socialLoginCheck	소셜 로그인 여부	false