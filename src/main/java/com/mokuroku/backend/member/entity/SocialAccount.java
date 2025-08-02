package com.mokuroku.backend.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "social_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member와 다대일 관계 (하나의 회원이 여러 소셜 계정 연결 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 50)
    private String provider;  // 예: "google", "naver", "kakao"

    @Column(name = "provider_id", nullable = false, length = 255)
    private String providerId;  // 소셜 로그인 제공자 고유 사용자 ID

    @Column(name = "linked_at")
    private LocalDateTime linkedAt;

    @PrePersist
    public void prePersist() {
        if (linkedAt == null) {
            linkedAt = LocalDateTime.now();
        }
    }
}
