package com.kalchelin.kalchelin_road.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

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

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException e) {
        log.warn("중복 이메일: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 4. 입력값 검증 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {

        // 검증 실패한 필드들을 우리 형식으로 변환
        List<ErrorResponse.FieldError> errors = e.getBindingResult()
                .getFieldErrors()                           // 스프링이 모아둔 실패 목록
                .stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(),                      // 필드명 (예: "title")
                        fe.getDefaultMessage())).toList();  // DTO에 적은 message

        log.warn("검증 실패: {}", errors);
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "입력값이 올바르지 않습니다",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 5. 요청 본문을 읽을 수 없음 (깨진 JSON, 변환 불가) -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("요청 본문 파싱 실패: {}", e.getMessage());    // 상세 내용은 로그에만
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "요청 본문의 형식이 올바르지 않습니다"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 6. 잘못된 파일 -> 400
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFile(InvalidFileException e) {
        log.warn("잘못된 파일 업로드: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 7. 스프링 자체 크기 제한 초과
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSize(MaxUploadSizeExceededException e) {
        log.warn("업로드 크기 초과: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(), "파일 크기는 10MB 이하여야 합니다.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(body);
    }

    // 8. 유효하지 않은/만료된 토큰 -> 400
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException e) {
        log.warn("유효하지 않은 토큰: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 9. 이메일 미인증 상태로 글/댓글 작성 시도 -> 403
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotVerified(EmailNotVerifiedException e) {
        log.warn("미인증 유저의 쓰기 시도: {}", e.getMessage());
        ErrorResponse body = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // 10. 그 외 예상 못 한 모든 예외 -> 500
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
