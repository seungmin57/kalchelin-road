package com.kalchelin.kalchelin_road.exception;

public class ResourceNotFoundException extends RuntimeException {

    // RuntimeException 상속 = 예외를 잡지 않아도 컴파일됨(unchecked)
    // Exception을 상속하면 호출하는 모든 곳에 try-catch를 강제해서 전역 처리를 하려는 목적과 정면으로 충돌한다
    public ResourceNotFoundException(String message) {
        super(message);     // 메시지를 부모(RuntimeException)에 넘겨 저장
    }
}
