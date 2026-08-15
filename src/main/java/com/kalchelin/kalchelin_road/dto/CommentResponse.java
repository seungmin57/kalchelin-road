package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String authorName;        // User 통째 아니라 이름만(프록시 방지)
    private final Long authorId;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorName = comment.getAuthor().getDisplayName();
        this.authorId = comment.getAuthor().getId();
        this.createdAt = comment.getCreatedAt();
    }
}
