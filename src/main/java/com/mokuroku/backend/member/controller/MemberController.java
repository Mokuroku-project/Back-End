package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import com.mokuroku.backend.member.dto.LoginRequestDTO;
import com.mokuroku.backend.member.dto.LoginResponseDTO;
import com.mokuroku.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/join", consumes = {"multipart/form-data"})
    public ResponseEntity<ResultDTO<RegisterResponseDTO>> register(
            @Valid @RequestPart("requestDTO") RegisterRequestDTO requestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        RegisterResponseDTO responseDTO = memberService.register(requestDTO, file);
        return ResponseEntity.ok(new ResultDTO<>("success", responseDTO));
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
    public ResponseEntity<ResultDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO responseDTO = memberService.login(loginRequestDTO);
        return ResponseEntity.ok(new ResultDTO<>("success", responseDTO));
    }

    @Operation(
            summary = "로그아웃",
            description = "현재 사용자의 액세스 토큰과 리프레시 토큰을 무효화합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @PostMapping("/logout")
    public ResponseEntity<ResultDTO<Void>> logout(
            @Parameter(description = "액세스 토큰", required = true)
            @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        memberService.logout(accessToken);
        return ResponseEntity.ok(new ResultDTO<>("success", null));
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "인증된 사용자의 계정을 비활성화하고 토큰을 무효화합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "이미 탈퇴된 계정"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @PostMapping("/withdrawal")
    public ResponseEntity<ResultDTO<Void>> withdraw(
            @Parameter(description = "액세스 토큰", required = true)
            @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        memberService.withdraw(accessToken);
        return ResponseEntity.ok(ResultDTO.<Void>builder()
                .status("success")
                .data(null)
                .build());
    }
}