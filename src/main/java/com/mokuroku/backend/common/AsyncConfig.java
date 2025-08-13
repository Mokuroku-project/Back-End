package com.mokuroku.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 비동기 작업을 위한 설정 클래스.
 * @EnableAsync를 활성화하고 ThreadPoolTaskExecutor를 구성하여 비동기 작업 처리.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 비동기 작업을 처리하는 ThreadPoolTaskExecutor 빈.
     * - corePoolSize: 기본 스레드 수 (2).
     * - maxPoolSize: 최대 스레드 수 (10).
     * - queueCapacity: 대기 큐 용량 (500).
     * @return Executor 비동기 작업 실행자
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본 스레드 수
        executor.setMaxPoolSize(10); // 최대 스레드 수
        executor.setQueueCapacity(500); // 대기 큐 용량
        executor.setThreadNamePrefix("Async-Thread-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}