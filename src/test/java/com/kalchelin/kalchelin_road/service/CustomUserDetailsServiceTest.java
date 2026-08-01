package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest {
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;

    @Test
    void 탈퇴한_계정은_로그인이_막힌다() {
        // Given: 탈퇴 상태인 유저
        User user = new User("gone", passwordEncoder.encode("pw1234"), Role.USER, "gone@test.com");
        user.withdraw();
        userRepository.save(user);
        em.flush();
        em.clear();

        // When & Then
        assertThatThrownBy(() ->
                userDetailsService.loadUserByUsername("gone"))
                                .isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    void 없는_아이디도_같은_예외가_난다() {
        // 탈퇴 계정과 없는 아이디의 반응이 같아야 계정 존재가 새지 않는다
        assertThatThrownBy(() ->
                userDetailsService.loadUserByUsername("nobody"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void 정상_계정은_UserDetails를_반환한다() {
        // Given
        User user = new User("alive", passwordEncoder.encode("pw1234"), Role.USER, "alive@test.com");
        userRepository.save(user);
        em.flush();
        em.clear();

        // When
        var details = userDetailsService.loadUserByUsername("alive");

        // Then
        assertThat(details.getUsername()).isEqualTo("alive");
    }
}
