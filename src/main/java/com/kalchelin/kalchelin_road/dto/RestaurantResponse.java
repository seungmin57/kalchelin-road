package com.kalchelin.kalchelin_road.dto;

import com.kalchelin.kalchelin_road.entity.Restaurant;
import lombok.Getter;

@Getter
public class RestaurantResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final String roadAddress;
    private final String region;
    private final Double longitude;
    private final Double latitude;
    // 평균 별점·리뷰 수는 집계 단계에서 추가

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.roadAddress = restaurant.getRoadAddress();
        this.region = restaurant.getRegion();
        this.longitude = restaurant.getLongitude();
        this.latitude = restaurant.getLatitude();
    }
}