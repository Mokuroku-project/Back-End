package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;

public interface MemberService {
    RegisterResponseDTO register(RegisterRequestDTO requestDTO);
}
