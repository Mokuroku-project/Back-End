package com.mokuroku.backend.member.service;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    ResponseEntity<ResultDTO> register(RegisterRequestDTO requestDTO, MultipartFile file);

    void verifyEmail(String email, String code);
    void resendVerificationCode(String email);

    @Transactional
    LoginResponseDTO login(LoginRequestDTO req, HttpServletResponse res);

    void logout(HttpServletRequest req, HttpServletResponse res);
}
