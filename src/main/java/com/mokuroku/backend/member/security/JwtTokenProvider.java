package com.mokuroku.backend.member.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.issuer}")
    private String issuer;

    /** 256bit 이상 랜덤 바이트의 Base64 문자열 */
    @Value("${jwt.secret-base64}")
    private String secretBase64;

    /** 초 단위 TTL */
    @Value("${jwt.access-ttl-seconds}")
    private long accessTtlSeconds;

    @Value("${jwt.refresh-ttl-seconds}")
    private long refreshTtlSeconds;

    /** 허용 시계 오차(초) – 선택, 기본 60초 권장 */
    @Value("${jwt.clock-skew-seconds:60}")
    private long clockSkewSeconds;

    private SecretKey signingKey;

    @PostConstruct
    void init() {
        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalStateException("jwt.secret-base64 is missing");
        }
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        // 길이가 짧으면 여기서 WeakKeyException 발생 → 환경설정 점검
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** Access Token 생성 (role 포함) */
    public String generateAccessToken(String email, String role) {
        return build(email, role, accessTtlSeconds);
    }

    /** Refresh Token 생성 (role 불필요) */
    public String generateRefreshToken(String email) {
        return build(email, null, refreshTtlSeconds);
    }

    private String build(String subject, String role, long ttlSeconds) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
                .header().type("JWT").and()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(signingKey, Jwts.SIG.HS256);

        if (role != null) {
            builder.claim("role", role);
        }
        return builder.compact();
    }

    /** 서명/만료/발급자 검증 후 JWS 반환 */
    public Jws<Claims> parseAndValidate(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .clockSkewSeconds(clockSkewSeconds)
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
    }

    /** 간단 유효성 체크 */
    public boolean isValid(String token) {
        try {
            parseAndValidate(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 편의 헬퍼들 */
    public String getEmailFromToken(String token) {
        return parseAndValidate(token).getPayload().getSubject();
    }

    public String getRoleFromToken(String token) {
        Object v = parseAndValidate(token).getPayload().get("role");
        return v != null ? v.toString() : null;
    }

    public long getAccessTtlSeconds()   { return accessTtlSeconds; }
    public long getRefreshTtlSeconds()  { return refreshTtlSeconds; }
}
