package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.OwnerReview;
import com.kalchelin.kalchelin_road.entity.Restaurant;

import java.util.List;


public record RestaurantDetailResponse(
        Long id,
        String name,
        String address,
        String roadAddress,
        String region,
        Double longitude,
        Double latitude,
        String kakaoPlaceUrl,
        Double averageRating,   // 리뷰가 없으면 null
        Long reviewCount,
        List<OwnerReviewResponse> ownerReviews
) {
    public static RestaurantDetailResponse of(Restaurant restaurant, RatingStats stats, List<OwnerReview> ownerReviews) {
        return new RestaurantDetailResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getRoadAddress(),
                restaurant.getRegion(),
                restaurant.getLongitude(),
                restaurant.getLatitude(),
                restaurant.getKakaoPlaceUrl(),
                round(stats.getAverage()),
                stats.getCount(),

                ownerReviews.stream().map(OwnerReviewResponse::new).toList());

    }

    /**
     * 4.3333333333을 4.3으로, null은 그대로 둔다 - 리뷰 없음과 0점은 다르다
     */
    private static Double round(Double value) {
        return value == null ? null : Math.round(value * 10) / 10.0;
    }
}
