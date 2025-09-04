package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import com.mokuroku.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "회원가입 요청을 처리하고, 가입된 회원 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이메일 또는 닉네임 중복")
    })
    @PostMapping("/join")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO) {

        RegisterResponseDTO responseDTO = memberService.register(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO responseDTO = memberService.login(loginRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}

/*
✅ 결과
Swagger UI 문서 예시는 다음과 같이 구성됩니다:
Tag: 회원
Summary: 회원가입
Description: 회원가입 요청을 처리하고, 가입된 회원 정보를 반환합니다.

응답코드
200: 회원가입 성공
400: 잘못된 요청 (DTO 검증 실패 등)
409: 이메일 또는 닉네임 중복

✅ 다음에 적용해볼 수 있는 Swagger 요소
@RequestBody(description = "...")
@Parameter(description = "...") (쿼리 파라미터 설명용)
@ApiResponse(content = @Content(...)) (복잡한 응답 모델 설명)
 */
