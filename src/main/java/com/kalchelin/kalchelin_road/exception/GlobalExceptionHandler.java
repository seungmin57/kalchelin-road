package com.kalchelin.kalchelin_road.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j  // Lombok: log 변수를 자동으로 만들어줌
@RestControllerAdvice   // = @ControllerAdvice + @ResponseBody (반환값을 JSON으로)
public class GlobalExceptionHandler {

    // 1. 리소스 없음 -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        log.warn("리소스 없음: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),       // 404 (숫자 직접 쓰는 것보다 안전)
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // 2. 권한 없음 -> 잡지 않고 다시 던진다
    // Security 필터의 ExceptionTranslationFilter가 처리하도록 넘김
    // 이게 없으면 아래 3이 가져가서 403이 500으로 망가진다
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateUsernameException e) {
        log.warn("중복 아이디: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 3. 그 외 예상 못 한 모든 예외 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("처리되지 않은 예외", e);
        ErrorResponse body  = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다"  // 클라이언트엔 내부 정보 숨김
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
