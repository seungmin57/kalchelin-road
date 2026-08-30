package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 5000, message = "내용은 5000자 이내여야 합니다")
    private String content;
    // author는 없다. 작성자는 클라이언트에서 보내는 게 아니라 세션에서 꺼낸다

    @NotNull(message = "평점은 필수입니다")
    @DecimalMin(value = "0.5", message = "평점은 0.5 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
    private Double rating;

    @Valid
    @NotNull(message = "가게 정보는 필수입니다")
    private RestaurantRequest restaurant;   // 통째로 중첩
}
