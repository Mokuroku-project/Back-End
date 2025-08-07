package com.mokuroku.backend.common;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder PasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/",
                            "/api",
                            "/auth/**",
                            "/products/**",
                            "/dutch/**",
                            "/swagger-ui.html",     // ✅ UI HTML 파일
                            "/swagger-ui/**",       // ✅ JS, CSS 리소스
                            "/v3/api-docs",         // ✅ JSON 문서
                            "/v3/api-docs/**",      // ✅ 그룹화된 문서
                            "/webjars/**",              // ✅ swagger-ui 리소스
                            "/favicon.ico",             // ✅ 404 방지
                            "/error"
                    ).permitAll()

                    .anyRequest().authenticated()
            )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                        .accessDeniedHandler(jwtAccessDeniedHandler)
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드를 설정합니다.
    configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더를 허용합니다.
    configuration.setAllowCredentials(true); // 쿠키 인증을 허용합니다.

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}