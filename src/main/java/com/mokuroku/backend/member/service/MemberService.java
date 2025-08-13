package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import com.mokuroku.backend.member.dto.VerifyEmailResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * 회원 관련 비즈니스 로직을 정의하는 서비스 인터페이스.
 * 회원가입, 이메일 인증, 인증 이메일 재전송, 이메일 전송 에러 조회 기능을 제공.
 */
public interface MemberService {

    /**
     * 회원가입 요청을 처리하고, 가입된 회원 정보를 반환.
     * 회원가입 후 이메일 인증을 위한 EmailVerification 레코드를 생성하고 인증 메일을 전송.
     *
     * @param requestDTO 회원가입 요청 데이터 (이메일, 비밀번호, 닉네임 등)
     * @param locale     다국어 지원을 위한 로케일 (예: ko, en)
     * @return RegisterResponseDTO 가입된 회원 정보
     * @throws CustomException 이메일 또는 닉네임 중복 시
     */
    RegisterResponseDTO register(RegisterRequestDTO requestDTO, Locale locale);

    VerifyEmailResponseDTO verifyEmail(String token);

    /**
     * 이메일 인증 토큰을 검증하고, 인증 완료 시 회원의 emailVerified 상태를 true로 업데이트.
     *
     * @param token 인증 토큰 (예: "550e8400-e29b-41d4-a716-446655440000")
     * @return VerifyEmailResponseDTO 인증 결과
     * @throws CustomException 토큰이 유효하지 않거나 만료된 경우
     */
    VerifyEmailResponseDTO verifyEmail(String token, Locale locale);

    /**
     * 인증 이메일을 재전송.
     * 미인증 상태의 회원에게 새 인증 토큰을 생성하여 이메일을 전송.
     *
     * @param email 재전송 대상 이메일 (예: "user@example.com")
     * @param locale 다국어 지원을 위한 로케일 (예: ko, en)
     * @return VerifyEmailResponseDTO 재전송 결과
     * @throws CustomException 이메일이 존재하지 않거나 이미 인증된 경우
     */
    VerifyEmailResponseDTO resendVerificationEmail(String email, Locale locale);

    /**
     * 이메일 전송 에러 상태를 조회.
     * Redis에 저장된 에러 메시지를 반환.
     *
     * @param email 조회 대상 이메일
     * @return String 에러 메시지, 없으면 null
     */
    String getEmailSendError(String email);
}