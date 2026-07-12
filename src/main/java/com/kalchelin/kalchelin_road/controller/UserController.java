package com.kalchelin.kalchelin_road.controller;


import com.kalchelin.kalchelin_road.dto.SignupRequest;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입: POST /api/users/signup
    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest request) {
        return userService.signup(request.getUsername(), request.getPassword());
    }
}
