package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security가 로그인 때 해당 username과 일치하는 사람을 찾아오라고 부르는 창구
// DB를 탐색해 UserDetails로 돌려주면, 비번 대조(matches)와 세션 저장은 Security가 알아서 한다.
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 가입 때 만든 findByUsername 재사용 (Optional로 옴)
        User user = userRepository.findByUsername(username)
                // 없으면 Security가 약속한 예외 -> 로그인 실패로 이어짐
                    .orElseThrow(() -> new UsernameNotFoundException("없는 아이디: " + username));


        if (user.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 계정입니다.");  // 로그인 차단
        }
        // 찾은 User를 UserDetails 규격으로 감싸 반환
        return new CustomUserDetails(user);
    }

}
