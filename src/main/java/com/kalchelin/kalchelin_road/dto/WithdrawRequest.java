package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequest {
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
