package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 아이디(username)로 사용자를 찾는 메서드 (로그인, 중복확인에 쓰임)
    Optional<User> findByUsername(String username);
}
