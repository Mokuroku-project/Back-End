package com.mokuroku.backend.member.service;

import com.mokuroku.backend.member.dto.TokenReissueRequestDTO;
import com.mokuroku.backend.member.dto.TokenResponseDTO;

public interface TokenService {
    TokenResponseDTO reissueToken(TokenReissueRequestDTO requestDTO);
}
