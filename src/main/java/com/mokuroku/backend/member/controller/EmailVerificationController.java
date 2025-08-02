package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.member.dto.EmailVerificationRequestDTO;
import com.mokuroku.backend.member.dto.EmailVerificationConfirmRequestDTO;
import com.mokuroku.backend.member.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "이메일 인증", description = "이메일 인증 관련 API")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "이메일 인증 요청", description = "이메일 주소로 인증 메일을 발송합니다.")
    @ApiResponse(responseCode = "200", description = "이메일 전송 성공")
    @PostMapping("/send")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody EmailVerificationRequestDTO requestDTO) {
        emailVerificationService.sendVerificationEmail(requestDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증 코드 검증", description = "이메일로 받은 인증 코드를 검증합니다.")
    @ApiResponse(responseCode = "200", description = "인증 성공")
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCode(@RequestBody EmailVerificationConfirmRequestDTO requestDTO) {
        boolean result = emailVerificationService.verifyCode(requestDTO.getEmail(), requestDTO.getCode());
        return ResponseEntity.ok(result);
    }
}