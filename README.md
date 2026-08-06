# 칼슐랭로드 (Kalchelin Road)

칼국수 맛집 리뷰 커뮤니티 백엔드 API

관리자가 직접 방문해 작성하는 **오너 평가**와, 일반 사용자가 남기는 **커뮤니티 리뷰**를 함께 제공합니다.

## 관련 저장소

- [프론트엔드](https://github.com/seungmin57/kalchelin-road-front) — React + Vite

## 기술 스택

- Java 21, Spring Boot 3.x
- Spring Security (세션 기반 인증)
- Spring Data JPA, H2 (개발) → MySQL (예정)
- JUnit 5, MockMvc

## 주요 기능

- **인증/인가** — 세션 로그인, 역할 기반 권한(ADMIN/USER), 소유권 검사
- **이메일 인증** — 토큰 발송 후 인증, 미인증 사용자 글·댓글 작성 차단
- **비밀번호 재설정** — 토큰 기반, 계정 존재 여부 노출 방지
- **회원 탈퇴** — 소프트 삭제, 작성 글의 작성자명 마스킹
- **커뮤니티** — 글·댓글 CRUD, 페이지네이션, 별점
- **파일 업로드** — 확장자·MIME·크기 검증

## 설계 상 고려한 점

- **N+1 해결** — `@EntityGraph`로 목록 조회 시 연관 엔티티 함께 로딩
- **세션 스냅샷 문제** — 세션의 `User`는 로그인 시점 상태이므로, 인증·권한 판단은 조회 시점의 DB 값으로 수행
- **응답 형식 통일** — Security 필터 단계 에러도 동일한 `ErrorResponse` JSON으로 반환
- **계정 열거 방지** — 탈퇴 계정과 존재하지 않는 계정에 동일한 응답

## 실행

```bash
./gradlew bootRun
```

`http://localhost:8080`에서 실행되며, API 문서는 `/swagger-ui/index.html`에서 확인할 수 있습니다.

메일 발송을 사용하려면 `src/main/resources/application-local.properties`에 SMTP 설정이 필요합니다.