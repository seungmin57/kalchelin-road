package com.kalchelin.kalchelin_road.config;

import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner = 앱이 완전히 뜬 직후 run()을 한 번 실행해주는 규격
// H2는 재시작하면 데이터가 날아가므로, 오너 계정을 매번 자동으로 심어준다
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;      // 여기서도 해싱해서 저장

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // 이미 있으면 다시 만들지 않음 (나중에 MySQL로 옮겨도 안전하게)
        if (userRepository.findByUsername("owner").isPresent()) {
            return;
        }
        // 오너 계정 생성 - 비밀번호는 반드시 해싱해서 저장 (회원가입과 동일)
        User owner = new User("owner", passwordEncoder.encode("owner1234"), Role.ADMIN, "owner@kalchelin.com");
        owner.verifyEmail();
        userRepository.save(owner);
        System.out.println("오너 계정 생성 완료: owner / owner1234");

    }


}
