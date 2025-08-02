package com.mokuroku.backend.member.service;

public interface EmailVerificationService {

    /**
     * 이메일로 인증 코드를 발송한다.
     *
     * @param email 인증 요청 대상 이메일
     */
    void sendVerificationEmail(String email);           // 인증 코드 전송

    /**
     * 이메일과 인증 코드를 비교하여 유효한지 검증한다.
     *
     * @param email 대상 이메일
     * @param code 사용자가 입력한 인증 코드
     * @return 인증 성공 여부 (true = 일치, false = 불일치 또는 만료)
     */
    boolean verifyCode(String email, String code);      // 인증 코드 검증
}

/*
✅ 사용 위치
컨트롤러: 이메일 인증 요청 API에서 사용
테스트 코드: 모킹(Mock) 가능하게 함
확장성: 구현체(EmailVerificationServiceImpl)를 유연하게 교체 가능

*/

