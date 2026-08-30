package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.dto.RatingStats;
import com.kalchelin.kalchelin_road.dto.RestaurantDetailResponse;
import com.kalchelin.kalchelin_road.dto.RestaurantRequest;
import com.kalchelin.kalchelin_road.entity.OwnerReview;
import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.Restaurant;
import com.kalchelin.kalchelin_road.exception.ResourceNotFoundException;
import com.kalchelin.kalchelin_road.repository.OwnerReviewRepository;
import com.kalchelin.kalchelin_road.repository.PostRepository;
import com.kalchelin.kalchelin_road.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final PostRepository postRepository;
    private final OwnerReviewRepository ownerReviewRepository;

    // 이미 등록된 가게면 그대로, 없으면 만든다
    // 글을 쓸 때마다 호출되므로 같은 가게에는 항상 같은 Restaurant가 붙는다
    @Transactional
    public Restaurant findOrCreate(RestaurantRequest request) {
        String placeKey = Restaurant.toPlaceKey(request.getLongitude(), request.getLatitude());

        return restaurantRepository.findByPlaceKey(placeKey)
                .orElseGet(() -> restaurantRepository.saveAndFlush(new Restaurant(
                        request.getName(), request.getAddress(), request.getRoadAddress(),
                        request.getRegion(), request.getLongitude(), request.getLatitude(),
                        request.getKakaoPlaceUrl())
                ));
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("가게를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Restaurant> findAll(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Restaurant> searchInDb(String name, Pageable pageable) {
        return restaurantRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public Restaurant update(Long id, RestaurantRequest request) {
        Restaurant restaurant = findById(id);
        restaurant.update(request.getName(), request.getAddress(), request.getRoadAddress(), request.getRegion());
        return restaurant;
    }

    /**
     * 가게 상세 - 집계와 오너 큐레이션을 함께 돌려준다.
     * 유저 리뷰 목록은 페이지네이션이 필요하므로 별도 엔드포인트로 뻈다
     */
    @Transactional(readOnly = true)
    public RestaurantDetailResponse findDetail(Long id) {
        Restaurant restaurant = findById(id);       // 없으면 여기서 404
        RatingStats stats = postRepository.findRatingStats(id);
        List<OwnerReview> ownerReviews = ownerReviewRepository.findByRestaurantId(id);

        return RestaurantDetailResponse.of(restaurant, stats, ownerReviews);
    }

}
