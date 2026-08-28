package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.OwnerReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;    //기본 저장/조회 기능을 제공하는 JPA 인터페이스를 가져옴

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {       // JpaRepository 상속을 통해 DI가 가능해짐
    @EntityGraph(attributePaths = {"restaurant"})
    Page<OwnerReview> findAll(Pageable pageable);
}
