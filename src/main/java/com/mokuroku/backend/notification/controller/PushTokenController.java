package com.mokuroku.backend.notification.controller;

import com.mokuroku.backend.notification.dto.MemberPushTokenDTO;
import com.mokuroku.backend.notification.service.PushTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.firebase.messaging.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/push/token")
public class PushTokenController {

  private final PushTokenService pushTokenService;

  @PostMapping()
  public void save(@RequestBody MemberPushTokenDTO pushTokenDTO) {
    pushTokenService.save(pushTokenDTO);
  }

  @DeleteMapping()
  public void revoke(@RequestBody MemberPushTokenDTO pushTokenDTO) {
    pushTokenService.revoke(pushTokenDTO);
  }
}
