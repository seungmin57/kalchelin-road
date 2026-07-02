package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.OwnerReview;     //다룰 데이터(엔티티) 가져오기
import com.kalchelin.kalchelin_road.repository.OwnerReviewRepository;        //DB 접근 도구(리포지토리) 가져오기
import org.springframework.stereotype.Service;        //@Service 어노테이션 가져오기

import java.util.List;

@Service
public class OwnerReviewService {
    private final OwnerReviewRepository ownerReviewRepository;

    // 생성자: Spring이 리포지토리를 자동으로 만들어서 여기에 넣어준다
    public OwnerReviewService(OwnerReviewRepository ownerReviewRepository) {
        this.ownerReviewRepository = ownerReviewRepository;
    }

    // [기능 1] 새 평가 저장하기
    public OwnerReview createReview(String title, String content, double rating) {
        OwnerReview review = new OwnerReview(title, content, rating); // 받은 값으로 새 평가 객체 생성
        return ownerReviewRepository.save(review);  // 리포지토리로 DB에 저장하고, 저장된 결과(id 채워진 상태)를 돌려줌
    }

    // [기능 2] 전체 평가 목록 조회하기
    public List<OwnerReview> getAllReviews() {
        return ownerReviewRepository.findAll();     // 리포지토리로 DB의 모든 평가를 가져와 목록으로 돌려줌
    }

}
