package com.kalchelin.kalchelin_road.config;

import org.springframework.context.annotation.Bean;         // @Bean 어노테이션
import org.springframework.context.annotation.Configuration;    // @Configuration 어노테이션
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 설정을 담는 클래스임을 표시
public class SecurityConfig {
    // 비밀번호 해싱 도구(PasswordEncoder)를 Spring 창고에 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();     // BCrypt 방식으로 해싱하는 도구를 만들어 등록
    }

    // 요청을 검사하는 문지기의 규칙표를 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // (1) 지금은 REST API라 CSRF 보호를 끔 (아래 설명)
                .csrf(csrf -> csrf.disable())
                // (2) 주소별 접근 규칙
                .authorizeHttpRequests(auth -> auth
                        // 오너 평가 "조회(GET)"는 누구나 허용
                        .requestMatchers(HttpMethod.GET, "/api/owner-reviews/**").permitAll()
                        // 회원가입은 누구나
                        .requestMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                        // 그 외 모든 요청은 로그인(인증)해야 함
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")   // 여기로 POST하면 로그인

                        // REST 스타일: 성공/실패 시 페이지 이동 대신 상태코드만 반환
                        .successHandler((req, res, auth) -> res.setStatus(200))     // 성공 -> 200
                        .failureHandler((req, res, ex)   -> res.setStatus(401))     // 실패 -> 401
                        .permitAll()        // 로그인 주소 자체는 누구나 접근 가능해야 함
                );

        return http.build();    // 위에서 정한 규칙으로 문지기를 완성해서 등록
    }
}
