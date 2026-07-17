package com.kalchelin.kalchelin_road.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    // author는 없다. 작성자는 클라이언트에서 보내는 게 아니라 세션에서 꺼낸다
}
