package com.kalchelin.kalchelin_road.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class KakaoSearchConfig {

    /**
     * KAKAO API 전용 HTTP 클라이언트.
     * 인증 헤더를 여기서 한 번만 박아두면 서비스는 키의 존재조차 모른다.
     */
    @Bean
    public RestClient KakaoRestClient(@Value("${kakao.rest-api-key}") String restApiKey) {
        // 타임아웃이 없으면 카카오가 느릴 때 우리 스레드가 무한정 붙잡힌다
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));       // 상대 서버와 연결이 맺어질 때까지
        factory.setReadTimeout(Duration.ofSeconds(5));          // 연결 후 응답이 도착할 때까지

        return RestClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + restApiKey)
                .requestFactory(factory)
                .build();
    }
}
