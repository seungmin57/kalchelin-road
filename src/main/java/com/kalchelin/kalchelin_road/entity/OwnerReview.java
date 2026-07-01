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

    public OwnerReview(String title, String content, int rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
    }
}
