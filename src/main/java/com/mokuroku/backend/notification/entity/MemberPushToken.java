package com.mokuroku.backend.notification.entity;

import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_push_token",
    uniqueConstraints = @UniqueConstraint(name="uq_push_token", columnNames="token"))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberPushToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberPushTokenId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "email")
  private Member member;

  private String token;
  private String platform;
  private String userAgent;
  private boolean revoked;
  private LocalDateTime createdDate;
  private LocalDateTime revokedDate;
}
