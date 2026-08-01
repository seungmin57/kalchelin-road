package com.kalchelin.kalchelin_road.controller;


import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import com.kalchelin.kalchelin_road.service.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // 인증된 유저를 만드는 헬퍼
    private User 인증된_유저(String username) {
        User user = new User(username, passwordEncoder.encode("pw1234"), Role.USER, username + "@test.com" );
        user.verifyEmail();     // 없으면 글 작성이 403으로 먼저 막힌다
        return userRepository.save(user);
    }

    @Test
    void 비로그인_상태로_글을_쓰면_401() throws Exception {
        mockMvc.perform(post("/api/posts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"제목\",\"content\":\"내용\",\"rating\":4.5}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 없는_글을_조회하면_404() throws Exception {
        mockMvc.perform(get("/api/posts/9999")
                .with(user(new CustomUserDetails(인증된_유저("reader")))))
                .andExpect(status().isNotFound());
    }
}
