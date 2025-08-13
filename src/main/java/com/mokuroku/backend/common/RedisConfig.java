package com.mokuroku.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 클래스.
 * RedisTemplate을 구성하여 문자열 키와 값을 처리.
 */
@Configuration
public class RedisConfig {

    /**
     * RedisTemplate 빈 설정.
     * 문자열 키와 값을 사용하며, 재전송 횟수 캐싱에 활용.
     * @param connectionFactory Redis 연결 팩토리
     * @return RedisTemplate<String, String> 문자열 기반 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}