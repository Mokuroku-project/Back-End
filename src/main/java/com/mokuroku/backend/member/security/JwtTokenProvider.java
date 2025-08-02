package com.mokuroku.backend.member.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}") // Access Token 유효시간 (ms)
    private long accessTokenValidityMs;

    @Value("${jwt.refresh-expiration}") // Refresh Token 유효시간 (ms)
    private long refreshTokenValidityMs;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // ✅ Access Token 생성
    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenValidityMs);
    }

    // ✅ Refresh Token 생성
    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenValidityMs);
    }

    // ✅ JWT 공통 생성 로직
    private String generateToken(String email, long validityMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT에서 이메일 추출
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // ✅ JWT 만료 시간 남은 ms 반환
    public long getExpiration(String token) {
        return parseClaims(token).getExpiration().getTime() - System.currentTimeMillis();
    }

    // ✅ JWT 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token); // 파싱 시 예외 발생하지 않으면 유효
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("비어있는 JWT claims: {}", e.getMessage());
        }
        return false;
    }

    // ✅ JWT 파싱 (공통)
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Refresh Token 유효기간 반환
    public long getRefreshExpirationMs() {
        return refreshTokenValidityMs;
    }
}


/*
✅ 주요 기능
메서드	설명
generateToken()	Access Token 생성
getUserEmailFromToken()	JWT에서 이메일 추출
validateToken()	서명, 만료 여부 등 토큰 유효성 검증
getExpiration()	만료일 추출 (선택)
*/