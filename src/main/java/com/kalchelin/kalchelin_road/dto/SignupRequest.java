package com.kalchelin.kalchelin_road.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    private String username;    // 가입할 아이디
    private String password;    // 가입할 비밀번호 (평문으로 들어옴 -> 저장 전에 해싱)
}
