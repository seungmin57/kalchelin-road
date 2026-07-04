package com.kalchelin.kalchelin_road.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter     // 필드 값을 읽는 getter 자동 생성
@Setter     // 필드에 값을 넣는 setter 자동 생성(요청 데이터를 담을 때 Spring이 씀)
@NoArgsConstructor  // 기본 생성자 자동 생성 (Spring이 빈 그릇을 먼저 만든 뒤 값을 채우기 때문에 필요)
public class OwnerReviewRequest {
    private String title;   // 브라우저가 보낸 제목이 담길 자리
    private String content; // 내용이 담길 자리
    private double rating;   // 평점이 담길 자리
}
