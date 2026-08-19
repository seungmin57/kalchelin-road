package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.dto.PageResponse;
import com.kalchelin.kalchelin_road.dto.RestaurantRequest;
import com.kalchelin.kalchelin_road.dto.RestaurantResponse;
import com.kalchelin.kalchelin_road.entity.Restaurant;
import com.kalchelin.kalchelin_road.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 가게 직접 등록 (ADMIN 보정용). 평소엔 글 작성 시 자동 등록된다
    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest request) {
        Restaurant saved = restaurantService.findOrCreate(request);
        return ResponseEntity.status(201).body(new RestaurantResponse(saved));
    }

    // 우리 DB에 등록된 가게 목록/검색
    @GetMapping
    public PageResponse<RestaurantResponse> list(
            @RequestParam(required = false) String name,
            @PageableDefault(size=12) Pageable pageable) {
        Page<Restaurant> page = (name == null || name.isBlank())
                ? restaurantService.findAll(pageable)
                : restaurantService.searchInDb(name, pageable);

        return new PageResponse<>(page.map(RestaurantResponse::new));
    }

    @GetMapping("/{id}")
    public RestaurantResponse detail(@PathVariable Long id) {
        return new RestaurantResponse(restaurantService.findById(id));
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @Valid @RequestBody RestaurantRequest request) {
        return new RestaurantResponse(restaurantService.update(id, request));
    }
}
