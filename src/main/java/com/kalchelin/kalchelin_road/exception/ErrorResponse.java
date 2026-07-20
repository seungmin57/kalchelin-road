package com.kalchelin.kalchelin_road.exception;

import lombok.Getter;

import java.time.LocalDateTime;

// PostResponse와 같은 성격의 DTO
@Getter
public class ErrorResponse {

    private final int status;               // 404, 400 등
    private final String message;           // 사람이 읽을 메시지
    private final LocalDateTime timestamp;  // 언제 발생했는지

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();   // 만들 때 자동으로 현재 시간
    }
}
