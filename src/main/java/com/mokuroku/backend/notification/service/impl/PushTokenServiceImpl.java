package com.mokuroku.backend.notification.service.impl;

import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.notification.dto.MemberPushTokenDTO;
import com.mokuroku.backend.notification.entity.MemberPushToken;
import com.mokuroku.backend.notification.repository.MemberPushTokenRepository;
import com.mokuroku.backend.notification.service.PushTokenService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushTokenServiceImpl implements PushTokenService {

  private final MemberRepository memberRepository;
  private final MemberPushTokenRepository pushTokenRepository;

  @Override
  public void save(MemberPushTokenDTO pushTokenDTO) {

    // 임시 테스트 이메일 -> 나중에는 accessToken에서 사용자 정보를 가져올 것임
    String email = "test@gmail.com";

    // 회원인지 검증 -> 회원상태에 대한 검증 추가 필요함
    Member member = memberRepository.findById(email)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    Optional<MemberPushToken> byToken = pushTokenRepository.findByToken(pushTokenDTO.getToken());

    if (byToken.isEmpty()) {
      MemberPushToken entity = MemberPushTokenDTO.toEntity(pushTokenDTO, member);
      pushTokenRepository.save(entity);
    } else if (byToken.get().isRevoked()) {
      MemberPushToken build = byToken.get().toBuilder()
          .revoked(false)
          .revokedDate(null)
          .build();
      pushTokenRepository.save(build);
    }
  }

  @Override
  public void revoke(MemberPushTokenDTO pushTokenDTO) {

    pushTokenRepository.findByToken(pushTokenDTO.getToken()).ifPresent(t ->
        pushTokenRepository.save(t.toBuilder()
            .revoked(true)
            .revokedDate(LocalDateTime.now())
            .build()
        )
    );
  }
}
