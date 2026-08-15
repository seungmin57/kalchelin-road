package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String authorName;        // User 통째가 아니라 '이름만'
    private final Long authorId;
    private final Double rating;
    private final LocalDateTime createdAt;

    // Post 엔티티를 받아서 필요한 것만 뽑아 담는 생성자
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getAuthor().getDisplayName();   // 여기서 프록시가 실제 조회됨
        this.authorId = post.getAuthor().getId();
        this.rating = post.getRating();
        this.createdAt = post.getCreatedAt();
    }

}
