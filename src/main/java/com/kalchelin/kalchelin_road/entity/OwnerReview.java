package com.kalchelin.kalchelin_road.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OwnerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private double rating;     // 1~5 점수

    @Column(nullable = true)    // 이미지가 없는 평가도 허용
    private String imageUrl;    // 저장된 이미지의 경로를 담는 자리(예: "uploads/abc.jpg")

    public OwnerReview(String title, String content, double rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    public void update(String title, String content, double rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    // 이미지 경로를 설정하는 메서드 (파일 저장 후 경로가 정해지면 호출)
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
