package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.OwnerReview;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OwnerReviewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final double rating;
    private final String imageUrl;
    private LocalDateTime createdAt;
    private final RestaurantResponse restaurant;

    public OwnerReviewResponse(OwnerReview review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.imageUrl = review.getImageUrl();
        this.restaurant = new RestaurantResponse(review.getRestaurant());
        this.createdAt = review.getCreatedAt();
    }
}
