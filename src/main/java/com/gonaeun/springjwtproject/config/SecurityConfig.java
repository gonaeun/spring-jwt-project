package com.gonaeun.springjwtproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",        // 회원가입/로그인
                    "/swagger-ui/**",      // Swagger UI
                    "/v3/api-docs/**"      // OpenAPI JSON
                ).permitAll()
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
