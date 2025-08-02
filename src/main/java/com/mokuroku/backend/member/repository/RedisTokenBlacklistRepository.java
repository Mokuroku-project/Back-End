package com.mokuroku.backend.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenBlacklistRepository {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    /**
     * 토큰을 블랙리스트에 등록합니다.
     * @param token 블랙리스트에 추가할 JWT 토큰
     * @param durationSeconds 토큰 만료까지 남은 시간(초 단위)
     */
    public void blacklistToken(String token, long durationSeconds) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "true", durationSeconds, TimeUnit.SECONDS);
    }

    /**
     * 토큰이 블랙리스트에 포함되어 있는지 확인합니다.
     * @param token 검사할 JWT 토큰
     * @return 블랙리스트에 있으면 true, 아니면 false
     */
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }
}
