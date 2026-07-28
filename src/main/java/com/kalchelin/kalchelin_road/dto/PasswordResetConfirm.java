package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordResetConfirm {
    @NotBlank(message = "토큰은 필수입니다")
    private String token;

    @NotBlank(message = "새 비밀번호는 필수입니다")
    @Size(min = 8, max = 64, message = "비밀번호는 8~64자여야 합니다")
    private String newPassword;
}
