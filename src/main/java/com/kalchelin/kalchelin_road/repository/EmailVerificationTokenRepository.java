package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.EmailVerificationToken;
import com.kalchelin.kalchelin_road.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    void deleteByUser(User user);   // 재발송 시 기존 토큰 정리용
}
