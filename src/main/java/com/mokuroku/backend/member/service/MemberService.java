package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;

public interface MemberService {
    RegisterResponseDTO register(RegisterRequestDTO requestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
