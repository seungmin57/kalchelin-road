package com.kalchelin.kalchelin_road.dto;

public record RestaurantSearchResponse(
        String name,
        String address,
        String roadAddress,
        String region,
        Double longitude,
        Double latitude,
        String kakaoPlaceUrl
) {}