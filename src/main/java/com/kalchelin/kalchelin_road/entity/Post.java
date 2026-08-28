package com.kalchelin.kalchelin_road.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)      // 연관관계 매핑 - "여러(Many) 글이 한(One) 작성자에 속한다
    @JoinColumn(name = "user_id", nullable = false)     // DB에 user_id 컬럼(FK)이 생김
    private User author;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Double rating;  // 별점 (0.5 ~ 5.0)

    // 가게 없는 글은 존재할 수 없다
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Post(String title, String content, User author, Double rating, Restaurant restaurant) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.rating = rating;
        this.restaurant = restaurant;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content, Double rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

}
