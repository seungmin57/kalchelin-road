package com.kalchelin.kalchelin_road.controller;


import com.kalchelin.kalchelin_road.dto.*;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.service.CustomUserDetails;
import com.kalchelin.kalchelin_road.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다. 로그인해주세요");
    }

    // 비밀번호 재설정 요청: POST /api/users/password-reset
    @PostMapping("/password-reset")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        userService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok("입력하신 이메일로 재설정 링크를 보냈습니다.");
    }

    // 실제 재설정: POST /api/users/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<String> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirm request) {
        userService.confirmPasswordReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 변경되었습니다. 새 비밀번호로 로그인해주세요.");
    }

    // 회원 탈퇴: DELETE /api/users/me
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody WithdrawRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdraw(userDetails.getUser(), request.getPassword());
        return ResponseEntity.noContent().build();      // 204
    }

    // 새로 고침 경우
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {


        // 세션 User는 로그인 시점 스냅샷 -> id만 믿고 다시 조회
        User user = userService.findById(userDetails.getUser().getId());

        return ResponseEntity.ok(new UserResponse(user));
    }
}
