package com.mokuroku.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
    private boolean socialLogin;
    private LocalDateTime regDate;
    private LocalDateTime withdrawalDate;
    private Role role;
    private Status status;

    public enum Role {
        USER, ADMIN;

        @JsonValue
        public String getValue() {
            return name().toLowerCase();
        }

        @JsonCreator
        public static Role fromValue(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    public enum Status {
        USABLE, UNUSABLE;

        @JsonValue
        public String getValue() {
            return name().toLowerCase();
        }

        @JsonCreator
        public static Role fromValue(String value) {
            return Role.valueOf(value.toUpperCase());
        }
    }
}