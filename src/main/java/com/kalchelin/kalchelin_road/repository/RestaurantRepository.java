package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // findOrCreate의 핵심 - 이미 등록된 가게인지 확인
    Optional<Restaurant> findByPlaceKey(String placeKey);

    // 우리 DB에 등록된 가게 검색 (외부 API 검색과는 별개)
    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
