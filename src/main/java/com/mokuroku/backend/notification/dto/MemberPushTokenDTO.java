package com.mokuroku.backend.notification.dto;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.notification.entity.MemberPushToken;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MemberPushTokenDTO {

  private String token;
  private String platform;
  private String userAgent;

  public static MemberPushToken toEntity(MemberPushTokenDTO dto, Member member) {
    return MemberPushToken.builder()
        .email(member)
        .token(dto.getToken())
        .platform(dto.getPlatform())
        .userAgent(dto.getUserAgent())
        .revoked(false)
        .createdDate(LocalDateTime.now())
        .build();
  }
}
