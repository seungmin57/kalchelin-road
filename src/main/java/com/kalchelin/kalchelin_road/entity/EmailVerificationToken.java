package com.kalchelin.kalchelin_road.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;       // 메일 링크에 들어갈 랜덤 문자열

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;          // 어느 계정의 토큰인지

    @Column(nullable = false)
    private LocalDateTime expiresAt;    // 만료 시각

    public EmailVerificationToken(String token, User user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    // 만료됐는지 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

}
