package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.TokenReissueRequestDTO;
import com.mokuroku.backend.member.dto.TokenResponseDTO;
import com.mokuroku.backend.member.service.AuthService;
import com.mokuroku.backend.member.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인 및 토큰 재발급 관련 API")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 통해 로그인하고 Access/Refresh 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
                    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
            }
    )
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/reissue")
    @Operation(
            summary = "토큰 재발급",
            description = "Refresh Token을 이용해 새로운 AccessToken과 RefreshToken을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재발급 성공",
                            content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "RefreshToken 유효성 실패", content = @Content)
            }
    )
    public ResponseEntity<TokenResponseDTO> reissue(@Valid @RequestBody TokenReissueRequestDTO request) {
        return ResponseEntity.ok(tokenService.reissueToken(request));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "Access Token을 블랙리스트에 등록하고, Refresh Token을 제거합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
            }
    )
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }

        String accessToken = bearerToken.substring(7); // "Bearer " 제거
        authService.logout(accessToken);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}


/*
✅ 기타 고려사항
JWT를 HttpOnly Cookie에 담을 경우 HttpServletResponse를 통해 Set-Cookie 헤더 설정 필요.
로그인 실패 예외는 @ControllerAdvice 로 처리하는 것이 좋습니다.
향후 RefreshToken 발급 시 Redis에 저장 후 함께 반환 가능.
 */