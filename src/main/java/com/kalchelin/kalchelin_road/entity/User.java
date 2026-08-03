package com.kalchelin.kalchelin_road.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.kalchelin.kalchelin_road.entity.Role;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")      // 테이블 이름을 users로 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)        // 비어있으면 안 되고 중복도 안 됨
    private String username;    // 로그인 아이디

    @JsonIgnore
    @Column(nullable = false)
    private String password;    // 비밀번호 (나중에 암호화해서 저장)

    @Enumerated(EnumType.STRING)    // enum을 글자 그대로 DB에 저장
    @Column(nullable = false)
    private Role role;      // 권한 (오너인지 일반 회원인지)

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailVerified = false; // 가입 시엔 미인증

    @Column(nullable = false)
    private boolean deleted = false;    // 탈퇴 여부

    private LocalDateTime deletedAt;    // 탈퇴 시각 (나중에 파기 배치용)

    public User(String username, String password, Role role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 탈퇴 처리
    public void withdraw() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 남에게 보여줄 이름. 탈퇴한 계정은 아이디를 가린다
    // getUsername()은 로그인, 본인 확인용
    public String getDisplayName() {
        return deleted ? "탈퇴한 사용자" : username;
    }

}
