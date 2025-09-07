package com.mokuroku.backend.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 정보를 나타내는 JPA 엔티티
 */
@Entity
@Table(name = "member", indexes = {
        @Index(name = "idx_nickname", columnList = "nickname")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 회원 이메일 (기본 키)
     */
    @Id
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /**
     * 암호화된 비밀번호
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 고유한 닉네임
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    /**
     * 프로필 이미지 경로 (URL 또는 파일 경로)
     */
    @Column(length = 512)
    private String profileImage;

    /**
     * 소셜 로그인 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "social_login_check", nullable = false, length = 10)
    private SocialLogin socialLoginCheck;

    /**
     * 가입 일시
     */
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    /**
     * 탈퇴 일시
     */
    @Column(name = "withdrawal_date")
    private LocalDateTime withdrawalDate;

    /**
     * 계정 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status;

    /**
     * 회원 역할
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @PrePersist
    public void prePersist() {
        regDate = LocalDateTime.now();
        if (status == null) {
            status = Status.USABLE;
        }
        if (role == null) {
            role = Role.USER;
        }
        if (socialLoginCheck == null) {
            socialLoginCheck = SocialLogin.GENERAL;
        }
    }

    public enum Role {
        USER, ADMIN
    }

    public enum Status {
        USABLE("1"),
        UNUSABLE("0");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Status fromValue(String value) {
            for (Status status : values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid status value: " + value);
        }
    }

    public enum SocialLogin {
        SOCIAL("1"),
        GENERAL("0");

        private final String value;

        SocialLogin(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SocialLogin fromValue(String value) {
            for (SocialLogin social : values()) {
                if (social.value.equals(value)) {
                    return social;
                }
            }
            throw new IllegalArgumentException("Invalid social login value: " + value);
        }
    }
}