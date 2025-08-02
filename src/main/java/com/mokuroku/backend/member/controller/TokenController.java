package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.member.dto.TokenReissueRequestDTO;
import com.mokuroku.backend.member.dto.TokenResponseDTO;
import com.mokuroku.backend.member.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "Access Token 재발급",
            description = "만료된 Access Token과 Refresh Token을 사용하여 새로운 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 토큰 요청"),
                    @ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않음")
            }
    )
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDTO> reissueToken(@Valid @RequestBody TokenReissueRequestDTO requestDTO) {
        TokenResponseDTO tokenResponse = tokenService.reissueToken(requestDTO);
        return ResponseEntity.ok(tokenResponse);
    }
}
