package com.mokuroku.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberDTO {
  
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    private boolean socialLogin; // true: 소셜, false: 일반
    private LocalDateTime regDate;
    private LocalDateTime withdrawalDate;
    private String role;   // "admin", "user"
    private boolean status; // true: usable, false: unusable
}