package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import com.mokuroku.backend.member.dto.ResendVerificationRequestDTO;
import com.mokuroku.backend.member.dto.VerifyEmailRequestDTO;
import com.mokuroku.backend.member.dto.VerifyEmailResponseDTO;
import com.mokuroku.backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * 회원 관련 API 엔드포인트를 제공하는 컨트롤러.
 * 회원가입, 이메일 인증, 인증 이메일 재전송, 에러 조회 기능을 처리.
 */
@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 요청을 처리.
     * 회원 정보를 등록하고 인증 이메일을 전송.
     *
     * @param requestDTO 회원가입 요청 데이터
     * @param locale     다국어 지원을 위한 로케일 (Accept-Language 헤더)
     * @return ResponseEntity<ResultDTO<RegisterResponseDTO>> 회원가입 결과
     */
    @PostMapping("/register")
    public ResponseEntity<ResultDTO<RegisterResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ko") Locale locale) {
        log.info("회원가입 요청: 이메일={}, 닉네임={}", requestDTO.getEmail(), requestDTO.getNickname());
        RegisterResponseDTO responseDTO = memberService.register(requestDTO, locale);
        log.debug("회원가입 응답: memberId={}", responseDTO.getMemberId());
        return ResponseEntity.ok(new ResultDTO<>("회원가입 성공", responseDTO));
    }

    /**
     * 이메일 인증 요청을 처리.
     * 인증 토큰을 검증하고 회원의 이메일 인증 상태를 업데이트.
     *
     * @param requestDTO 인증 요청 데이터 (토큰 포함)
     * @return ResponseEntity<ResultDTO<VerifyEmailResponseDTO>> 인증 결과
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ResultDTO<VerifyEmailResponseDTO>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequestDTO requestDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ko") Locale locale) {
        log.info("이메일 인증 요청: 토큰={}", requestDTO.getToken());
        VerifyEmailResponseDTO responseDTO = memberService.verifyEmail(requestDTO.getToken(), locale);
        log.debug("이메일 인증 응답: success={}", responseDTO.isSuccess());
        return ResponseEntity.ok(new ResultDTO<>("이메일 인증 처리 완료", responseDTO));
    }

    /**
     * 인증 이메일 재전송 요청을 처리.
     * 미인증 회원에게 새 인증 이메일을 전송.
     *
     * @param requestDTO 재전송 요청 데이터 (이메일 포함)
     * @param locale     다국어 지원을 위한 로케일 (Accept-Language 헤더)
     * @return ResponseEntity<ResultDTO<VerifyEmailResponseDTO>> 재전송 결과
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ResultDTO<VerifyEmailResponseDTO>> resendVerification(
            @Valid @RequestBody ResendVerificationRequestDTO requestDTO,
            @RequestHeader(value = "Accept-Language", defaultValue = "ko") Locale locale) {
        log.info("인증 이메일 재전송 요청: 이메일={}", requestDTO.getEmail());
        VerifyEmailResponseDTO responseDTO = memberService.resendVerificationEmail(requestDTO.getEmail(), locale);
        log.debug("인증 이메일 재전송 응답: success={}", responseDTO.isSuccess());
        return ResponseEntity.ok(new ResultDTO<>("인증 이메일 재전송 완료", responseDTO));
    }

    /**
     * 이메일 전송 에러를 조회.
     * Redis에 저장된 에러 메시지를 반환.
     *
     * @param email 조회 대상 이메일
     * @return ResponseEntity<ResultDTO<String>> 에러 메시지
     */
    @GetMapping("/email-error/{email}")
    public ResponseEntity<ResultDTO<String>> getEmailSendError(@PathVariable String email) {
        log.info("이메일 전송 에러 조회: 이메일={}", email);
        String errorMessage = memberService.getEmailSendError(email);
        return ResponseEntity.ok(new ResultDTO<>("이메일 전송 에러 조회 완료", errorMessage));
    }
}