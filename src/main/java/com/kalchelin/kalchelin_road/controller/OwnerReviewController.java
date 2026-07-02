package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.entity.OwnerReview;     // 돌려줄 데이터(엔티티) 가져오기
import com.kalchelin.kalchelin_road.service.OwnerReviewService;  // 일을 시킬 대상(서비스) 가져오기
import org.springframework.web.bind.annotation.GetMapping;      // GET 요청 연결 어노테이션
import org.springframework.web.bind.annotation.RequestMapping;  // 공통 주소 어노테이션
import org.springframework.web.bind.annotation.RestController;  // 웹 컨트롤러 어노테이션

import java.util.List;
@RestController                             // 이 클래스는 웹 요청을 받고, 결과를 데이터(JSON)으로 돌려주는 컨트롤러 표시
@RequestMapping("/api/owner-reviews")       // 이 컨트롤러의 모든 기능은 이 주소로 시작한다 표시
public class OwnerReviewController {
    private final OwnerReviewService ownerReviewService;

    // 생성자: Spring이 서비스를 자동으로 넣어줌(DI)
    public OwnerReviewController(OwnerReviewService ownerReviewService) {
        this.ownerReviewService = ownerReviewService;
    }

    @GetMapping     // Get 요청이 이 주소(/api/owner-reviews)로 오면 아래 기능을 실행해라
    public List<OwnerReview> getAllReviews() {
        return ownerReviewService.getAllReviews();
    }

}
