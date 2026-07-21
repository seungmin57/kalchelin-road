package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 1000, message = "댓글 내용은 1000자 이내여야 합니다")
    private String content;     // 어느 글인지는 URL로, 누구인지는 세션으로 -> content만 받음
}
