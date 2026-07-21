package com.kalchelin.kalchelin_road.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter     // 필드 값을 읽는 getter 자동 생성
@Setter     // 필드에 값을 넣는 setter 자동 생성(요청 데이터를 담을 때 Spring이 씀)
@NoArgsConstructor  // 기본 생성자 자동 생성 (Spring이 빈 그릇을 먼저 만든 뒤 값을 채우기 때문에 필요)
public class OwnerReviewRequest {
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다")
    private String title;   // 브라우저가 보낸 제목이 담길 자리

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 5000, message = "내용은 5000자 이내여야 합니다")
    private String content; // 내용이 담길 자리

    @NotNull(message = "평점은 필수입니다")
    @DecimalMin(value = "0.5", message = "평점은 0.5 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
    private Double rating;   // 평점이 담길 자리
}
