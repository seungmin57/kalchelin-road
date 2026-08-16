package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import com.kalchelin.kalchelin_road.service.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OwnerReviewControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void 일반_유저가_오너평가를_쓰면_403() throws Exception {
        // Given: ROLE_USER인 일반회원
        User normal = userRepository.save(
                new User("normal", passwordEncoder.encode("pw1234"), Role.USER, "n@test.com"));

        // When & Then: hasRole("ADMIN") 규칙에 막혀야 한다
        mockMvc.perform(post("/api/owner-reviews")
                .with(user(new CustomUserDetails(normal)))
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {"title":"제목","content":"내용","rating":4.5}
                                """))
                .andExpect(status().isForbidden());
    }
}
