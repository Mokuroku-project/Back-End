package com.mokuroku.backend.common.configuration;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import com.mokuroku.backend.member.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtFilter;
    private final AuthenticationEntryPoint authEntryPoint;   // 401
    private final AccessDeniedHandler accessDeniedHandler;   // 403

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ 일관된 DSL 스타일
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/members/login", "/members/join",
                                "/members/verify-email", "/members/verify-email/resend").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        .requestMatchers(
                                "/",
                                "/webjars/**",
                                "/auth/**",
                                "/products/**",
                                "/dutch/**",
                                "/sns/**",
                                "/bookmark/**",
                                "/swagger-ui/**",
                                "/v3/api-docs",         // ✅ JSON 문서
                                "/v3/api-docs/**",      // ✅ 그룹화된 문서
                                "/webjars/**",          // ✅ swagger-ui 리소스
                                "/favicon.ico",         // ✅ 404 방지
                                "/error").permitAll()
                        // ✅ (선택) 프리플라이트 허용 – CORS 세팅이 있다면 보통 필요 없지만 명시해도 무방
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // ✅ 그 외는 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드를 설정합니다.
    configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더를 허용합니다.
    configuration.setAllowCredentials(true); // 쿠키 인증을 허용합니다.
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder PasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
