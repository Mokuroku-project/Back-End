package com.mokuroku.backend.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {
    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;
    private String role; // "admin" 또는 "user"
    private boolean status; // true: usable, false: unusable
    private LocalDateTime regDate;
}
