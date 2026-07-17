package com.kalchelin.kalchelin_road.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    private String content;     // 어느 글인지는 URL로, 누구인지는 세션으로 -> content만 받음
}
