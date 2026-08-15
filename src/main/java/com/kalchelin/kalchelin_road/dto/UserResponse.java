package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final boolean emailVerified;
    // password 없음 - 절대 내보내지 않는다

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.emailVerified = user.isEmailVerified();
    }
}
