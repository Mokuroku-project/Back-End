package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface MemberService {

    /**
     * 회원가입을 처리하고, 가입된 회원 정보를 반환
     * @param requestDTO 회원가입 요청 데이터
     * @param file 프로필 이미지 파일 (선택 사항)
     * @return RegisterResponseDTO 회원가입 응답 데이터
     */
    RegisterResponseDTO register(RegisterRequestDTO requestDTO, MultipartFile file);

    /**
     * 로그인 요청을 처리하고, JWT 토큰을 포함한 응답을 반환
     * @param loginRequestDTO 로그인 요청 데이터 (이메일, 비밀번호)
     * @return LoginResponseDTO 로그인 응답 데이터 (이메일, 닉네임, 토큰)
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    /**
     * 로그아웃을 처리하여 액세스 토큰과 리프레시 토큰을 무효화
     * @param accessToken 무효화할 액세스 토큰
     */
    void logout(String accessToken);
}