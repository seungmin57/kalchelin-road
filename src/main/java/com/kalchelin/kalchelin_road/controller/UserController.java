package com.kalchelin.kalchelin_road.controller;


import com.kalchelin.kalchelin_road.dto.SignupRequest;
import com.kalchelin.kalchelin_road.dto.UserResponse;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입: POST /api/users/signup
    @PostMapping("/signup")
    public UserResponse signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.signup(request.getUsername(), request.getPassword(), request.getEmail());
        return new UserResponse(user);
    }

    // 이메일 인증: GET /api/users/verify?token=xxx
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다. 로그인해주세요");
    }
}
