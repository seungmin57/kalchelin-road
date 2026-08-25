package com.kalchelin.kalchelin_road.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 카카오 키워드 장소 검색 응답을 그대로 받는 그릇
 * 읽기만 하므로 record가 맞다
 */
public record KakaoLocalResponse(List<Document> documents) {
    public record Document(
            @JsonProperty("place_name") String placeName,
            @JsonProperty("address_name") String addressName,
            @JsonProperty("road_address_name") String roadAddressName,
            @JsonProperty("place_url") String placeUrl,
            String x,    // 경도 (실수 문자열)
            String y    // 위도 (실수 문자열)
    ) {}
}
