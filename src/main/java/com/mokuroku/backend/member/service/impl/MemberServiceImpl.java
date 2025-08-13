package com.mokuroku.backend.member.service.impl;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.mokuroku.backend.common.EmailService; // 이메일 전송 서비스
import com.mokuroku.backend.exception.ErrorCode; // 커스텀 예외의 에러 코드
import com.mokuroku.backend.exception.impl.CustomException; // 커스텀 예외 클래스
import com.mokuroku.backend.member.dto.RegisterRequestDTO; // 회원가입 요청 DTO
import com.mokuroku.backend.member.dto.RegisterResponseDTO; // 회원가입 응답 DTO
import com.mokuroku.backend.member.dto.VerifyEmailResponseDTO; // 이메일 인증/재전송 응답 DTO
import com.mokuroku.backend.member.entity.EmailVerification; // 이메일 인증 엔티티
import com.mokuroku.backend.member.entity.Member; // 회원 엔티티
import com.mokuroku.backend.member.entity.Member.Status; // 회원 상태 열거형
import com.mokuroku.backend.member.entity.Member.Role; // 회원 권한 열거형
import com.mokuroku.backend.member.repository.EmailVerificationRepository; // 이메일 인증 데이터 리포지토리
import com.mokuroku.backend.member.repository.MemberRepository; // 회원 데이터 리포지토리
import com.mokuroku.backend.member.service.MemberService; // 회원 서비스 인터페이스
import lombok.RequiredArgsConstructor; // Lombok으로 final 필드에 대한 생성자 자동 생성
import lombok.extern.slf4j.Slf4j; // SLF4J 로깅을 위한 어노테이션
import org.springframework.context.MessageSource; // 다국어 메시지 처리를 위한 Spring의 MessageSource
import org.springframework.context.NoSuchMessageException; // 메시지 조회 실패 예외
import org.springframework.data.redis.core.RedisTemplate; // Redis 데이터 처리를 위한 템플릿
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화를 위한 클래스
import org.springframework.stereotype.Service; // Spring의 서비스 컴포넌트를 나타내는 어노테이션
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리를 위한 어노테이션
import java.time.LocalDateTime; // 날짜 및 시간 처리를 위한 클래스
import java.util.Locale; // 다국어 처리를 위한 로케일 클래스
import java.util.Optional; // Optional을 사용하여 null 안전성 보장
import java.util.UUID; // UUID 생성을 위한 클래스
import java.util.concurrent.TimeUnit; // Redis TTL 설정을 위한 시간 단위

/**
 * MemberService 인터페이스의 구현 클래스.
 * 회원가입, 이메일 인증, 재전송, 에러 조회 로직 처리.
 * 다국어 지원을 위해 MessageSource와 Locale 사용.
 * Redis 캐싱과 DB 폴백으로 재전송 횟수 관리, 상세 로깅 추가.
 * Setter 대신 빌더 패턴(toBuilder)을 사용하여 엔티티 업데이트.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final int MAX_RESEND_COUNT = 5;              // 최대 이메일 재전송 횟수 상수
    private static final long ERROR_CACHE_TTL_MINUTES = 60;     // Redis 에러 캐시의 TTL (60분)

    // Redis 키 접두사 상수
    private static final String RESEND_COUNT_KEY = "email:verification:resend:"; // 재전송 횟수 키
    private static final String EMAIL_SEND_ERROR_KEY = "email:send:error:"; // 이메일 전송 에러 키
    private static final String TOKEN_KEY = "verify:token:"; // 인증 토큰 키

    // 의존성 주입 필드
    private final MemberRepository memberRepository; // 회원 데이터 리포지토리
    private final EmailVerificationRepository emailVerificationRepository; // 이메일 인증 데이터 리포지토리
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final EmailService emailService; // 이메일 전송 서비스
    private final RedisTemplate<String, String> redisTemplate; // Redis 데이터 처리
    private final MessageSource messageSource; // 다국어 메시지 처리

    /**
     * MessageSource에서 다국어 메시지를 조회하고, 실패 시 폴백 처리.
     * @param code 메시지 코드
     * @param args 메시지에 삽입할 인자
     * @param locale 다국어 로케일
     * @return 조회된 메시지 또는 코드 자체 (폴백)
     */
    private String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("Message not found: code={}, locale={}", code, locale);
            return code; // 메시지 없으면 코드 반환
        } catch (Exception e) {
            log.error("Message retrieval failed: code={}, locale={}, error={}", code, locale, e.getMessage());
            return code; // 예외 발생 시 코드 반환
        }
    }

    /**
     * 회원가입 요청을 처리하고, 가입된 회원 정보를 반환.
     * 회원가입 후 이메일 인증을 위한 EmailVerification 레코드를 생성하고 인증 메일을 전송.
     *
     * @param requestDTO 회원가입 요청 데이터 (이메일, 비밀번호, 닉네임 등)
     * @param locale     다국어 지원을 위한 로케일 (예: ko, en)
     * @return RegisterResponseDTO 가입된 회원 정보
     * @throws CustomException 이메일 또는 닉네임 중복 시
     */
    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO, Locale locale) {
        String email = requestDTO.getEmail().toLowerCase(); // 이메일 소문자 정규화

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(email)) {
            log.warn("회원가입 실패: 이미 사용 중인 이메일 - {}", email);
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL, messageSource, new Object[]{email});
        }
        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(requestDTO.getNickname())) {
            log.warn("회원가입 실패: 이미 사용 중인 닉네임 - {}", requestDTO.getNickname());
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME, messageSource, new Object[]{requestDTO.getNickname()});
        }

        // 비밀번호 검증: 소셜 로그인 아닐 경우 비밀번호 필수
        if (!requestDTO.isSocialLoginCheck() && (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank())) {
            log.warn("회원가입 실패: 비밀번호 누락 - {}", email);
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED, messageSource);
        }
        // 비밀번호 암호화 (소셜 로그인 아닌 경우)
        String encodedPassword = requestDTO.isSocialLoginCheck() ? null : passwordEncoder.encode(requestDTO.getPassword());

        // 회원 엔티티 생성 및 저장
        Member member = Member.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(requestDTO.getNickname())
                .profileImage(requestDTO.getProfileImage())
                .socialLogin(requestDTO.isSocialLoginCheck())
                .emailVerified(false)
                .regDate(LocalDateTime.now())
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();
        Member saved = memberRepository.save(member);
        log.info("회원가입 성공: memberId={}, 이메일={}", saved.getId(), saved.getEmail());

        // 인증 토큰 생성 및 EmailVerification 저장
        String token = UUID.randomUUID().toString();
        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .token(token)
                .verified(false)
                .resendCount(0)
                .build();
        emailVerificationRepository.save(verification);
        log.info("EmailVerification 저장: 이메일={}, 토큰={}", email, token);

        // Redis에 재전송 횟수 및 토큰 저장
        String redisResendKey = RESEND_COUNT_KEY + email;
        String redisTokenKey = TOKEN_KEY + token;
        try {
            redisTemplate.opsForValue().set(redisResendKey, "0", 24, TimeUnit.HOURS);
            redisTemplate.opsForValue().set(redisTokenKey, email, 10, TimeUnit.MINUTES);
            log.debug("Redis 설정 성공: resendKey={}, tokenKey={}", redisResendKey, redisTokenKey);
        } catch (Exception e) {
            log.warn("Redis 설정 실패: resendKey={}, tokenKey={}, 오류={}", redisResendKey, redisTokenKey, e.getMessage());
        }

        // 인증 이메일 전송
        try {
            emailService.sendVerificationEmail(email, token, locale);
        } catch (Exception e) {
            log.error("이메일 전송 실패: 이메일={}, 오류={}", email, e.getMessage());
            // 비동기 이메일 전송이므로 회원가입은 완료
        }

        // 회원가입 응답 DTO 생성 및 반환
        return RegisterResponseDTO.builder()
                .memberId(saved.getId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .profileImage(saved.getProfileImage())
                .role(RegisterResponseDTO.Role.valueOf(saved.getRole().name()))
                .regDate(saved.getRegDate())
                .build();
    }

    /**
     * 이메일 인증 요청을 처리 (기본 로케일: 한국어).
     * @param token 인증 토큰
     * @return VerifyEmailResponseDTO 인증 결과
     */
    @Override
    public VerifyEmailResponseDTO verifyEmail(String token) {
        return verifyEmail(token, Locale.KOREAN); // 기본 로케일로 한국어 사용
    }

    /**
     * 이메일 인증 요청을 처리.
     * Redis와 DB를 통해 토큰 유효성을 확인하고, 회원과 인증 상태를 업데이트.
     *
     * @param token  인증 토큰
     * @param locale 다국어 지원을 위한 로케일
     * @return VerifyEmailResponseDTO 인증 결과
     * @throws CustomException 토큰이 유효하지 않거나, 이미 인증되었거나, 만료된 경우
     */
    @Override
    @Transactional
    public VerifyEmailResponseDTO verifyEmail(String token, Locale locale) {
        // 토큰 유효성 검증
        if (token == null || token.trim().isEmpty() || !isValidUUID(token)) {
            log.warn("이메일 인증 실패: 유효하지 않은 토큰 - {}", token);
            throw new CustomException(ErrorCode.INVALID_TOKEN, messageSource);
        }

        // Redis에서 토큰 조회
        String redisTokenKey = TOKEN_KEY + token;
        String email = null;
        try {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisTokenKey))) {
                email = redisTemplate.opsForValue().get(redisTokenKey);
                log.debug("Redis 토큰 조회 성공: tokenKey={}, 이메일={}", redisTokenKey, email != null ? email : "없음");
            } else {
                log.debug("Redis 토큰 없음: tokenKey={}", redisTokenKey);
            }
        } catch (Exception e) {
            log.warn("Redis 토큰 조회 실패: tokenKey={}, 오류={}", redisTokenKey, e.getMessage(), e);
        }

        // Redis 조회 실패 시 DB 조회
        Optional<EmailVerification> verificationOpt;
        if (email != null && !email.trim().isEmpty()) {
            verificationOpt = emailVerificationRepository.findByEmailAndToken(email, token);
        } else {
            verificationOpt = emailVerificationRepository.findByToken(token);
        }

        if (verificationOpt.isEmpty()) {
            log.warn("이메일 인증 실패: 유효하지 않은 토큰 - {}", token);
            throw new CustomException(ErrorCode.INVALID_TOKEN, messageSource);
        }

        EmailVerification verification = verificationOpt.get();
        if (verification.isVerified()) {
            log.warn("이메일 인증 실패: 이미 인증된 토큰 - {}", token);
            throw new CustomException(ErrorCode.ALREADY_VERIFIED, messageSource);
        }

        // 토큰 만료 확인
        LocalDateTime expiryTime = verification.getExpiryTime();
        if (expiryTime.isBefore(LocalDateTime.now())) {
            log.warn("이메일 인증 실패: 만료된 토큰 - {}", token);
            throw new CustomException(ErrorCode.EXPIRED_TOKEN, messageSource);
        }

        // 회원 정보 조회
        Optional<Member> memberOpt = memberRepository.findByEmail(verification.getEmail());
        if (memberOpt.isEmpty()) {
            log.error("이메일 인증 실패: 회원 정보 없음 - {}", verification.getEmail());
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource);
        }

        // 회원 및 인증 상태 업데이트
        Member member = memberOpt.get();
        Member updatedMember = member.toBuilder()
                .emailVerified(true)
                .build();
        memberRepository.save(updatedMember); // 이메일 인증 상태 저장
        log.info("이메일 인증 성공: memberId={}, 이메일={}", updatedMember.getId(), updatedMember.getEmail());

        EmailVerification updatedVerification = verification.toBuilder()
                .verified(true)
                .build();
        emailVerificationRepository.save(updatedVerification); // @PreUpdate 호출 (resendCount 변경 없으므로 영향 없음)

        // Redis 토큰 키 삭제
        try {
            redisTemplate.delete(redisTokenKey);
            log.debug("Redis 토큰 키 삭제 성공: {}", redisTokenKey);
        } catch (Exception e) {
            log.warn("Redis 토큰 키 삭제 실패: tokenKey={}, 오류={}", redisTokenKey, e.getMessage(), e);
        }

        // 성공 메시지 반환
        String successMessage = getMessage("email.verification.success", null, locale);
        return new VerifyEmailResponseDTO(true, successMessage);
    }

    /**
     * UUID 형식의 토큰 유효성 검증.
     * @param token 검증할 토큰
     * @return 유효하면 true, 아니면 false
     */
    private boolean isValidUUID(String token) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        return token != null && token.matches(uuidPattern);
    }

    /**
     * 이메일 인증 재전송 요청을 처리.
     * 재전송 횟수와 시간 제한을 확인 후 새로운 또는 기존 토큰으로 이메일 전송.
     *
     * @param email  재전송 대상 이메일
     * @param locale 다국어 지원을 위한 로케일
     * @return VerifyEmailResponseDTO 재전송 결과
     * @throws CustomException 회원 없음, 이미 인증, 횟수 초과, 빈번한 요청 시
     */
    @Override
    @Transactional
    public VerifyEmailResponseDTO resendVerificationEmail(String email, Locale locale) {
        email = email.toLowerCase(); // 이메일 소문자 정규화

        // 회원 정보 확인
        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        if (memberOpt.isEmpty()) {
            log.warn("이메일 재전송 실패: 회원 정보 없음 - {}", email);
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource);
        }

        Member member = memberOpt.get();
        if (member.isEmailVerified()) {
            log.warn("이메일 재전송 실패: 이미 인증된 이메일 - {}", email);
            throw new CustomException(ErrorCode.ALREADY_VERIFIED, messageSource);
        }

        // 재전송 횟수 확인 (Redis 우선, DB 폴백)
        String redisKey = RESEND_COUNT_KEY + email;
        int resendCount;
        try {
            String countStr = redisTemplate.opsForValue().get(redisKey);
            resendCount = countStr != null ? Integer.parseInt(countStr) : 0;
            log.debug("Redis resendCount 조회 성공: 키={}, 값={}", redisKey, resendCount);
        } catch (Exception e) {
            log.warn("Redis resendCount 조회 실패: 키={}, 오류={}, DB로 폴백", redisKey, e.getMessage());
            Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByEmailAndVerifiedFalse(email);
            resendCount = verificationOpt.map(EmailVerification::getResendCount).orElse(0);
            log.info("DB resendCount 조회: 이메일={}, 값={}", email, resendCount);
        }

        // 재전송 횟수 제한 확인
        if (resendCount >= 5) {
            log.warn("이메일 재전송 실패: 재전송 횟수 제한 초과 - {}", email);
            throw new CustomException(ErrorCode.RESEND_LIMIT_EXCEEDED, messageSource);
        }

        // 시간 기반 재전송 제한 확인
        Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByEmailAndVerifiedFalse(email);
        LocalDateTime now = LocalDateTime.now();
        if (verificationOpt.isPresent()) {
            EmailVerification verification = verificationOpt.get();
            if (verification.getLastResendDate() != null &&
                    verification.getLastResendDate().isAfter(now.minusMinutes(1))) {
                log.warn("이메일 재전송 실패: 너무 빈번한 요청 - {}", email);
                throw new CustomException(ErrorCode.TOO_FREQUENT_EMAIL, messageSource);
            }

            // 재전송 횟수 갱신 및 일일 리셋
            int newResendCount = resendCount + 1;
            if (verification.getLastResendDate() != null &&
                    verification.getLastResendDate().toLocalDate().isBefore(now.toLocalDate())) {
                newResendCount = 1; // 일일 리셋
                log.info("resendCount 리셋: 이메일={}, 새로운 값={}", email, newResendCount);
            }

            // EmailVerification 업데이트
            EmailVerification updatedVerification = verification.toBuilder()
                    .resendCount(newResendCount)
                    .lastResendDate(now)
                    .build();
            emailVerificationRepository.save(updatedVerification);

            // Redis 재전송 횟수 업데이트
            try {
                redisTemplate.opsForValue().set(redisKey, String.valueOf(newResendCount), 24, TimeUnit.HOURS);
                log.debug("Redis resendCount 업데이트 성공: 키={}, 값={}", redisKey, newResendCount);
            } catch (Exception e) {
                log.warn("Redis resendCount 업데이트 실패: 키={}, 오류={}", redisKey, e.getMessage());
            }

            // 기존 토큰으로 이메일 재전송
            emailService.sendVerificationEmail(email, verification.getToken(), locale);
            log.info("기존 토큰으로 이메일 재전송: 이메일={}, 토큰={}", email, verification.getToken());
            String successMessage = getMessage("email.resend.success", null, locale);
            return new VerifyEmailResponseDTO(true, successMessage);
        } else {
            // 새로운 토큰 생성 및 저장
            String newToken = UUID.randomUUID().toString();
            EmailVerification newVerification = EmailVerification.builder()
                    .email(email)
                    .token(newToken)
                    .verified(false)
                    .resendCount(1)
                    .lastResendDate(now)
                    .build();
            emailVerificationRepository.save(newVerification);

            // Redis에 새로운 토큰 및 재전송 횟수 저장
            try {
                redisTemplate.opsForValue().set(redisKey, "1", 24, TimeUnit.HOURS);
                redisTemplate.opsForValue().set(TOKEN_KEY + newToken, email, 10, TimeUnit.MINUTES);
                log.debug("Redis 설정 성공: resendKey={}, tokenKey={}", redisKey, TOKEN_KEY + newToken);
            } catch (Exception e) {
                log.warn("Redis 설정 실패: resendKey={}, tokenKey={}, 오류={}", redisKey, TOKEN_KEY + newToken, e.getMessage());
            }

            // 새로운 토큰으로 이메일 재전송
            emailService.sendVerificationEmail(email, newToken, locale);
            log.info("새 토큰으로 이메일 재전송: 이메일={}, 토큰={}", email, newToken);
            String successMessage = getMessage("email.resend.success", null, locale);
            return new VerifyEmailResponseDTO(true, successMessage);
        }
    }

    /**
     * 이메일 전송 에러를 Redis에서 조회.
     * @param email 조회할 이메일
     * @return 에러 메시지 또는 null
     */
    @Override
    public String getEmailSendError(String email) {
        String redisKey = EMAIL_SEND_ERROR_KEY + email.toLowerCase();
        try {
            String error = redisTemplate.opsForValue().get(redisKey);
            log.debug("Redis 이메일 에러 조회 성공: 키={}, 값={}", redisKey, error != null ? error : "없음");
            return error;
        } catch (Exception e) {
            log.warn("Redis 이메일 에러 조회 실패: 키={}, 오류={}", redisKey, e.getMessage());
            return null;
        }
    }
}