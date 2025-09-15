package com.mokuroku.backend.notification.service;

import com.mokuroku.backend.notification.dto.MemberPushTokenDTO;
import org.springframework.stereotype.Service;

@Service
public interface PushTokenService {

  void save(MemberPushTokenDTO pushTokenDTO);

  void revoke(MemberPushTokenDTO pushTokenDTO);
}
