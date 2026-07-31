package com.kalchelin.kalchelin_road.service;


import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.InvalidPasswordException;
import com.kalchelin.kalchelin_road.exception.ResourceNotFoundException;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest     // 진짜 스프링 앱을 띄운다 (빈 주입, DB 연결 전부 실제)
@Transactional      // 각 테스트가 끝나면 자동 롤백 -> DB에 아무것도 안 남는다
public class UserServiceTest {

    // 평소 생성자 주입을 쓰지만, 테스트는 우리가 new 하는 게 아니라
    // JUnit이 인스턴스를 만들기 때문에 필드 주입(@Autowired)을 쓴다
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;

    @Test
    void 탈퇴하면_deleted가_true가_된다() {
        // Given: 테스트에 필여한 상태를 만든다
        User user = userRepository.save(
                new User("tester", passwordEncoder.encode("pw1234"), Role.USER, "t@test.com"));
        em.flush();     // 대기 중인 INSERT를 DB로 밀어낸다
        em.clear();     // 영속성 컨텍스트를 비운다 -> user가 detached가 된다


        // When: 검증하려는 동작을 실행한다
        userService.withdraw(user, "pw1234");

        // Then: 기대한 결과가 됐는지 확인한다
        em.flush();     // UPDATE를 실제로 발행시킨다
        em.clear();     // 캐시를 비워야 findById가 진짜 DB를 읽는다
        User found = userRepository.findById(user.getId()).orElseThrow();
        assertThat(found.isDeleted()).isTrue();
    }

    @Test
    void 틀린_비밀번호로_탈퇴하면_예외가_난다() {
        // Given
        User user = userRepository.save(
                new User("tester2", passwordEncoder.encode("pw1234"), Role.USER, "t2@test.com"));
        em.flush();
        em.clear();

        // When & Then
        // "이 코드를 실행하면" + "이 예외가 나야 한다"
        assertThatThrownBy(() -> userService.withdraw(user, "틀린비번"))
                .isInstanceOf(InvalidPasswordException.class);
    }
}
