package com.kalchelin.kalchelin_road.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

// PostResponse와 같은 성격의 DTO
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null인 필드는 JSON에서 아예 빼기
public class ErrorResponse {

    private final int status;               // 404, 400 등
    private final String message;           // 사람이 읽을 메시지
    private final LocalDateTime timestamp;  // 언제 발생했는지
    private final List<FieldError> errors;  // 검증 에러일 때만 채워짐

    // 기존 생성자 (404, 409,500용) - errors는 null
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    // 검증 에러용 생성자
    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();   // 만들 때 자동으로 현재 시간
        this.errors = errors;
    }

    //필드 하나의 에러 정보를 담는 중첩 클래스
    @Getter
    public static class FieldError {
        private final String field;
        private final String reason;

        public FieldError(String field, String reason) {
            this.field = field;
            this.reason = reason;
        }
    }
}
