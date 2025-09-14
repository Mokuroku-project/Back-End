package com.mokuroku.backend.member.controller;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseCookie;
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
  @PostMapping(value = "/join", consumes = "multipart/form-data", produces = "application/json")
  public ResponseEntity<ResultDTO<Void>> register(
          @Valid @RequestPart("requestDTO") RegisterRequestDTO requestDTO,
          @RequestPart(value = "file", required = false) MultipartFile file
  ) {
      // Service는 보통 DTO/void 반환이 깔끔하지만, 현재 시그니처에 맞춰 사용
      ResponseEntity<ResultDTO> result = memberService.register(requestDTO, file);
      // 제네릭이 안 맞는 경우를 피하려면 서비스도 ResultDTO<Void>로 통일 추천
      return ResponseEntity.status(result.getStatusCode()).body(result.getBody());
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ResultDTO<Void>> verifyEmail(@RequestBody @Valid VerifyEmailRequest req) {
      memberService.verifyEmail(req.getEmail(), req.getCode());
      return ResponseEntity.ok(new ResultDTO<>("OK", null));
  }

  @PostMapping("/verify-email/resend")
  public ResponseEntity<ResultDTO<Void>> resend(@RequestBody @Valid EmailOnlyDTO req) {
      memberService.resendVerificationCode(req.getEmail());
      return ResponseEntity.ok(new ResultDTO<>("OK", null));
  }

  @Operation(
          summary = "로그인",
          description = "이메일과 비밀번호로 로그인하여 JWT Access 토큰을 발급합니다.",
          operationId = "loginWithEmailPassword",
          tags = {"Auth"}
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "로그인 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청"),
          @ApiResponse(responseCode = "401", description = "인증 실패")
  })
  @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ResultDTO<LoginResponseDTO>> login(
          @Valid @RequestBody LoginRequestDTO loginRequestDTO,
          HttpServletResponse res
  ) {
      // 서비스 내부에서:
      // - BCrypt 검증
      // - Access 발급(roles 포함)
      // - (선택) Refresh 발급→ Redis/DB 저장 → HttpOnly+Secure 쿠키로 set
      //   res.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
      LoginResponseDTO body = memberService.login(loginRequestDTO, res);
      return ResponseEntity.ok()
              .cacheControl(CacheControl.noStore()) // ← 민감 응답 캐시 금지
              .body(new ResultDTO<>("OK", body)); // // ← 문자열 "success" 대신 표준 코드
  }
}