package com.mokuroku.backend.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

  @Id
  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, unique = true, length = 50)
  private String nickname;

  @Column(length = 512)
  private String profileImage;

  @Column(name = "social_login_check", length = 1)
  private String socialLoginCheck;  // '1' = 소셜 로그인, '0' = 일반

  @Column(name = "reg_date")
  private LocalDateTime regDate;

  @Column(name = "withdrawal_date")
  private LocalDateTime withdrawalDate;

  @Column(length = 1)
  private String status; // '1' = 사용 가능, '0' = 정지/탈퇴, '2' = 임시가입

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;  // ✅ Role 필드 추가

  @PrePersist
  public void prePersist() {
    regDate = LocalDateTime.now();
    if (status == null) {
      status = "1";
    }
    if (role == null) {
      role = Role.USER;
    }
  }

  public enum Role {
    USER, ADMIN
  }
}