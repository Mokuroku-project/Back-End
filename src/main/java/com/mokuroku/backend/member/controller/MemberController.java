package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.service.MemberService;
//import com.mokuroku.backend.member.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입, 로그인, 토큰 재발급 및 로그아웃 관련 API")
public class MemberController {

    //    private final TokenService tokenService;
    private final MemberService memberService;

    // ✅ 회원가입
    @PostMapping("/register")
    @Operation(
            summary = "회원가입",
            description = "회원가입 요청을 처리하고 가입된 회원 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = RegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "409", description = "이메일 또는 닉네임 중복", content = @Content)
    })
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO) {
        RegisterResponseDTO responseDTO = memberService.register(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}