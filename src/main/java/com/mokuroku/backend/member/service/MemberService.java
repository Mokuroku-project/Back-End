package com.mokuroku.backend.member.service;

//import com.mokuroku.backend.member.dto.LoginRequestDTO;
//import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;

public interface MemberService {

    // ✅ 회원가입
    RegisterResponseDTO register(RegisterRequestDTO requestDTO);

//    // ✅ 로그인
//    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
//
//    // ✅ 로그아웃
//    void logout(String accessToken);
}