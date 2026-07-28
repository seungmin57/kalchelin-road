package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.PasswordResetToken;
import com.kalchelin.kalchelin_road.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);   // 재요청 시 기존 토큰 정리
}
