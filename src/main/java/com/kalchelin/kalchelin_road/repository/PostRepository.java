package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.dto.RatingStats;
import com.kalchelin.kalchelin_road.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    @EntityGraph(attributePaths = {"author", "restaurant"})
    // 몸통은 여전히 Spring Data가 만들어주고, 우리는 "author도 같이 채워와라"만 얹는다.
    Page<Post> findAll(Pageable pageable);

    // 가게 상세의 리뷰 목록
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findByRestaurantId(Long restaurantId, Pageable pageable);

    // 평균 별점과 리뷰 수를 한 번에
    @Query("select avg(p.rating) as average, count(p) as count " + "from Post p where p.restaurant.id = :restaurantId")
    RatingStats findRatingStats(@Param("restaurantId") Long restaurantId);
}
