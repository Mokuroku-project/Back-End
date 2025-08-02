package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    void logout(String accessToken);
}
