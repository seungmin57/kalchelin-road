package com.kalchelin.kalchelin_road.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalchelin.kalchelin_road.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;         // @Bean 어노테이션
import org.springframework.context.annotation.Configuration;    // @Configuration 어노테이션
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration  // 설정을 담는 클래스임을 표시
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 비밀번호 해싱 도구(PasswordEncoder)를 Spring 창고에 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();     // BCrypt 방식으로 해싱하는 도구를 만들어 등록
    }

    // 요청을 검사하는 문지기의 규칙표를 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                // (1) 지금은 REST API라 CSRF 보호를 끔 (아래 설명)
                .csrf(csrf -> csrf.disable())
                // (2) 주소별 접근 규칙
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        // 오너 평가 "조회(GET)"는 누구나 허용
                        .requestMatchers(HttpMethod.GET, "/api/owner-reviews/**").permitAll()
                        // 오너 평가 "쓰기"는 ADMIN(오너)만 (POST/PUT/DELETE)가 여기로 온다
                        .requestMatchers("/api/owner-reviews/**").hasRole("ADMIN")
                        // 회원가입은 누구나, 이메일 인증 링크는 누구나(로그인 안 한 상태)
                        .requestMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/verify").permitAll()
                        // 글 조회는 누구나
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        // 비밀번호 재설정
                        .requestMatchers(HttpMethod.POST, "/api/users/password-reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/reset-password").permitAll()
                        // 그 외 모든 요청은 로그인(인증)해야 함
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")   // 여기로 POST하면 로그인

                        // REST 스타일: 성공/실패 시 페이지 이동 대신 상태코드만 반환
                        .successHandler((req, res, auth) -> res.setStatus(200))     // 성공 -> 200
                        .failureHandler((req, res, ex)   -> writeError(res, 401, "아이디 또는 비밀번호가 올바르지 않습니다."))     // 실패 -> 401
                        .permitAll()        // 로그인 주소 자체는 누구나 접근 가능해야 함
                )

                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))   // 302대신 200
                )

                // (3) Security 필터 단계에서 막힌 요청의 응답
                //     리다이렉트(302) 대신 상태 코드 + ErrorResponse JSON을 직접 내려준다.
                //     컨트롤러에 도달하지 못한 요청이라 GlobalExceptionHandler가 잡을 수 없다.
                .exceptionHandling(ex -> ex
                        // 로그인 안 된 상태로 접근 -> 401
                        .authenticationEntryPoint((req, res, authEx) ->
                                writeError(res, 401, "로그인이 필요합니다."))

                        // 로그인은 됐지만 권한 부족 -> 403
                        .accessDeniedHandler((req, res, deniedEx) ->
                                writeError(res, 403, "접근 권한이 없습니다."))
                );

        return http.build();    // 위에서 정한 규칙으로 문지기를 완성해서 등록
    }

    // ErrorResponse를 JSON으로 직접 써 내려간다
    // 필터 단계라 MVC의 자동 JSON 변환이 없어서 응답에 직접 써야 한다
    private void writeError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(res.getWriter(), new ErrorResponse(status, message));
    }
}
