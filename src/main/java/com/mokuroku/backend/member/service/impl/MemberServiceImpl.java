package com.mokuroku.backend.member.service.impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.common.component.MailComponents;
import com.mokuroku.backend.common.component.S3Uploader;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.security.JwtTokenProvider;
import com.mokuroku.backend.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Long MAIL_EXPIRES_IN = 300000L; // 5분
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif"); // 허용 가능한 이미지 파일 형식

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final RedisTemplate<String, String> redisTemplate;
    private final MailComponents mailComponents;
    private final PasswordEncoder passwordEncoder;      // ✅ 주입 필요
    private final JwtTokenProvider jwtTokenProvider;    // ✅ 주입 필요 (0.12.x 버전)
    private static final String EMAIL_JOIN_PREFIX = "join:";  // ✅ 공백 없음으로 통일

    @Override
    @Transactional
    public ResponseEntity<ResultDTO> register(RegisterRequestDTO requestDTO, MultipartFile file) {

        // 중복 이메일 검사
        if (memberRepository.existsById(requestDTO.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER);
        }

        // 중복 닉네임 검사
        if (memberRepository.existsByNickname(requestDTO.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 프로필 이미지 파일 있는지 확인
        if (file != null && !file.isEmpty()) { // 있을 경우 이미지 파일 검사 후 회원정보 저장
            // 파일 이름에서 확장자 추출
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

            String imageUrl;
            // 확장자가 이미지 파일인지 확인
            if (fileExtension != null && ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
                try {
                    imageUrl = s3Uploader.upload(file, "profile-images");
                } catch (Exception e) {
                    throw new CustomException(ErrorCode.FAILED_IMAGE_SAVE);
                }
            } else { // 이미지 파일이 아닌 경우에 대한 처리
                throw new CustomException(ErrorCode.UN_SUPPORTED_IMAGE_TYPE);
            }
        }

        String imageUrl = null;

        // 3) 회원 저장 (임시 가입 상태 "2")
        //    RegisterRequestDTO.joinMember가 내부에서 BCrypt를 사용한다면 OK.
        //    만약 서비스의 PasswordEncoder를 쓰고 싶으면 joinMember를 바꾸거나 여기서 인코딩하세요.
        Member member = RegisterRequestDTO.joinMember(requestDTO, imageUrl); // ✅ null → imageUrl
        memberRepository.save(member);

        // 회원정보 저장 후 가입한 이메일로 본인인증 메일 전송 및 레디스에 토큰값 저장
        String code = generateRandomCode6();

        try {
            sendVerificationEmail(requestDTO.getEmail(), code,
                    "MOKUROKU 회원가입 인증메일", EMAIL_JOIN_PREFIX);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_CONNECTION_FAILED);
        }

        return ResponseEntity.ok(
                new ResultDTO<>("회원가입에 성공했습니다. 인증을 위해 가입한 이메일의 메일을 확인해주세요.", null)
        );

    }

    // 6자리 코드 생성
    private static String generateRandomCode6() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900_000) + 100_000; // [100000, 999999]
        return String.valueOf(code);
    }

    private void sendVerificationEmail(String email, String code, String title, String redisKeyPrefix) {
        String key = redisKeyPrefix + email;  // ✅ "join:" + email
        String message = "<h3>5분안에 인증번호를 입력해주세요</h3> <br><h1>" + code + "</h1>";

        // 기존 코드가 있다면 삭제
        String prev = redisTemplate.opsForValue().get(key);
        if (prev != null) {
            redisTemplate.delete(key);
        }
        // 메일 전송
        mailComponents.sendMail(email, title, message);
        // Redis에 저장 (5분 TTL)
        redisTemplate.opsForValue().set(key, code, MAIL_EXPIRES_IN, TimeUnit.MILLISECONDS);
    }

    @Transactional
    @Override
    public void verifyEmail(String email, String code) {
        String key = EMAIL_JOIN_PREFIX + email;
        String saved = redisTemplate.opsForValue().get(key);

        if (saved == null || !saved.equals(code)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        int updated = memberRepository.activateByEmail(email); // ★ 강제 UPDATE
        if (updated == 0) {
            // 0이면 두 경우: (1) 이미 활성(= status '1') 이었거나 (2) 회원 없음
            if (!memberRepository.existsById(email)) {
                throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            }
        }
        redisTemplate.delete(key);          // 일회성 코드 삭제
    }

    public void resendVerificationCode(String email) {
      // 이미 활성화된 계정이면 스킵/예외
      Member m = memberRepository.findById(email)
              .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
      if ("1".equals(m.getStatus())) {
          throw new CustomException(ErrorCode.ACCOUNT_DISABLED); // or ALREADY_VERIFIED 같은 전용 코드
      }

      String code = generateRandomCode6();
      String key = EMAIL_JOIN_PREFIX + email;

      String prev = redisTemplate.opsForValue().get(key);
      if (prev != null) {
          redisTemplate.delete(key);
      }
      redisTemplate.opsForValue().set(key, code, MAIL_EXPIRES_IN, TimeUnit.MILLISECONDS);
      mailComponents.sendMail(email, "MOKUROKU 이메일 인증",
              "<h3>5분안에 인증번호를 입력해주세요</h3><h1>" + code + "</h1>");

      // 새 코드 생성/저장/메일 전송 (가입 때와 동일)
//      String code = generateRandomUUID(); // 6자리
//      if (redisTemplate.opsForValue().get(EMAIL_JOIN_PREFIX + email) != null) {
//          redisTemplate.delete(EMAIL_JOIN_PREFIX + email);
//      }
//      redisTemplate.opsForValue().set(EMAIL_JOIN_PREFIX + email, code, MAIL_EXPIRES_IN, TimeUnit.MILLISECONDS);
//      mailComponents.sendMail(email, "MOKUROKU 이메일 인증", "<h3>5분안에 인증번호를 입력해주세요</h3><h1>" + code + "</h1>");
    }

    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO req, HttpServletResponse res) {
        // 1) 사용자 조회
        Member m = memberRepository.findById(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // 2) 상태/타입 체크
        if (!"1".equals(m.getStatus())) {
            throw new CustomException(ErrorCode.ACCOUNT_SUSPENDED); // 상태값에 맞게 변경
        }
        if ("1".equals(m.getSocialLoginCheck())) {
            throw new CustomException(ErrorCode.SOCIAL_ACCOUNT_ONLY);
        }
        // 3) 비밀번호 검증
        if (!passwordEncoder.matches(req.getPassword(), m.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        // 4) Access Token 발급 (role 포함)
        String accessToken = jwtTokenProvider.generateAccessToken(m.getEmail(), m.getRole().name());

        // 5) (선택) Refresh Token 발급 & 저장 & 쿠키 세팅
        //    프로젝트에서 Refresh 전략을 쓴다면 아래 주석을 풀어 구현
        /*
        String refreshToken = jwtTokenProvider.generateRefreshToken(m.getEmail());
        // Redis/DB에 refreshToken 저장 (기기/패밀리 관리 포함 권장)
        redisTemplate.opsForValue().set("RT:" + refreshJti, refreshToken,
                                        jwtTokenProvider.getRefreshTtlSeconds(), TimeUnit.SECONDS);
        // HttpOnly + Secure 쿠키로 내려주기
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(jwtTokenProvider.getRefreshTtlSeconds())
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        */

        // 6) 응답 DTO
        return LoginResponseDTO.builder()
                .email(m.getEmail())
                .nickname(m.getNickname())
                .accessToken(accessToken)
                .refreshToken(null) // 쿠키 전략이면 본문에 넣지 않는 걸 권장
                .build();
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        String refresh = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refresh_token".equals(c.getName())) {
                    refresh = c.getValue();
                    break;
                }
            }
        }

        // (선택) refresh 파싱하여 jti/subject 꺼내기 → Redis 키 제거
        if (refresh != null && !refresh.isBlank()) {
            try {
                // jwtTokenProvider.parseAndValidate(refresh);
                // String jti = jwtTokenProvider.getJti(refresh);  // jti를 쓴다면
                // redisTemplate.delete("RT:" + jti);              // 혹은 "RT:{email}:{device}"
                // 또는 email로 관리했다면 해당 키 삭제 로직
            } catch (Exception ignored) {
                // 만료/위조여도 쿠키만 만료시키면 됨
            }
        }

        // 클라이언트 쿠키 즉시 만료 (path/도메인 반드시 동일)
        ResponseCookie expired = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh")                 // 발급 때와 동일해야 함
                .maxAge(0)
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, expired.toString());

        // (선택) SecurityContext 정리
        SecurityContextHolder.clearContext();
    }
}