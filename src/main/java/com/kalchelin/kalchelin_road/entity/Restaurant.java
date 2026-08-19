package com.kalchelin.kalchelin_road.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 같은 가게를 두 번 만들지 않기 위해 식별 키. 좌표 기반으로 만든다
    @Column(nullable = false, unique = true, length = 50)
    private String placeKey;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;         // 지번 주소

    @Column(length = 200)
    private String roadAddress;      // 도로명 주소 (없을 수 있음)

    @Column(nullable = false, length = 50)
    private String region;          // 카드에 표시할 짧은 지역명

    // 지도 표시용. WGS84 경위도
    @Column(nullable = false)
    private Double longitude;       // 경도(x)

    @Column(nullable = false)
    private Double latitude;        // 위도 (y)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Restaurant(String name, String address, String roadAddress, String region, Double longitude, Double latitude) {
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.region = region;
        this.longitude = longitude;
        this.latitude = latitude;
        this.placeKey = toPlaceKey(longitude, latitude);
        this.createdAt = LocalDateTime.now();
    }

    public static String toPlaceKey(Double longitude, Double latitude) {
        return String.format(Locale.ROOT,"%.6f_%.6f", longitude, latitude);
    }

    // 좌표와 placeKey는 바꾸지 않는다
    public void update(String name, String address, String roadAddress, String region) {
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.region = region;
    }

}
