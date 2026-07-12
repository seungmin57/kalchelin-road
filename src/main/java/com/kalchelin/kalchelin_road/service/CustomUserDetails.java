package com.kalchelin.kalchelin_road.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.kalchelin.kalchelin_road.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Security는 우리 User가 아니라 'UserDetails' 규격만 이해한다.
// 우리 User를 그 규격으로 감싸주는 어댑터 구현
public class CustomUserDetails implements UserDetails {
    private final User user;    // 우리 엔티티를 품고, 물으면 꺼내서 답한다

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 로그인한 사람의 원본 User가 나중에 필요할 때 꺼내는 통로 (커뮤니티 단계에서 씀)
    public User getUser() {
        return user;
    }

    // (1) 권한 목록: Security의 "ROLE_" 접두사 규칙. ROLE.USER -> "ROLE_USER"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    // (2) Security가 matches()로 대조할 '저장된 해시' - 가입 때 encode()로 넣은 값
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // (3) 로그인 아이디로 쓸 값
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // (4) 계정 상태 플래그 4종 - 지금은 만료/잠금 기능이 없으니 전부 true(정상)
    @Override public boolean isAccountNonExpired() {return true;}   // 계정 자체가 만료됐는가
    @Override public boolean isAccountNonLocked() {return true;}    // 계정이 잠겼는가
    @Override public boolean isCredentialsNonExpired() {return true;}   // 비밀번호(자격증명)가 만료됐는가
    @Override public boolean isEnabled() {return true;}     // 계정이 활성화됐는가
}
