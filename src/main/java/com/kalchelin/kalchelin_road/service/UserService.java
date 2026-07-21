package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.DuplicateEmailException;
import com.kalchelin.kalchelin_road.exception.DuplicateUsernameException;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;      // @Bean으로 등록한 해싱 도구

    // 생성자 주입: Repository와 PasswordEncoder 둘 다 Spring이 넣어줌
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입: 아이디 중복 확인 -> 비밀번호 해싱 -> 저장
    public User signup(String username, String password, String email) {
        // (1) 이미 있는 아이디면 거부
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsByEmail((email))) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다");
        }

        // (2) 비밀번호를 해싱 (평문 -> 알아볼 수 없는 문자열)
        String encodedPassword = passwordEncoder.encode(password);

        // (3) User 객체 생성 (권한은 일단 USER로)
        User user = new User(username, encodedPassword, Role.USER, email);

        // (4) DB에 저장
        return userRepository.save(user);
    }
}
