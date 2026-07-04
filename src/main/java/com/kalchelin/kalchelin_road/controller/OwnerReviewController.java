package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.dto.OwnerReviewRequest; // 데이터 요청 그릇 가져오기
import com.kalchelin.kalchelin_road.entity.OwnerReview;     // 돌려줄 데이터(엔티티) 가져오기
import com.kalchelin.kalchelin_road.service.OwnerReviewService;  // 일을 시킬 대상(서비스) 가져오기
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController                             // 이 클래스는 웹 요청을 받고, 결과를 데이터(JSON)으로 돌려주는 컨트롤러 표시
@RequestMapping("/api/owner-reviews")       // 이 컨트롤러의 모든 기능은 이 주소로 시작한다 표시
public class OwnerReviewController {
    private final OwnerReviewService ownerReviewService;

    // 생성자: Spring이 서비스를 자동으로 넣어줌(DI)
    public OwnerReviewController(OwnerReviewService ownerReviewService) {
        this.ownerReviewService = ownerReviewService;
    }

    @GetMapping     // GET 요청이 이 주소(/api/owner-reviews)로 오면 아래 기능을 실행해라
    public List<OwnerReview> getAllReviews() {
        return ownerReviewService.getAllReviews();
    }

    @PostMapping    // POST 요청이 이 주소로 오면 아래 메서드 실행
    public OwnerReview createReview(@RequestBody OwnerReviewRequest request) {
        // @RequestBody = 브라우저가 보낸 JSON을 위에서 만든 OwnerReviewRequest 그릇에 담아라
        return ownerReviewService.createReview(     // 그릇에서 값을 꺼내 서비스에 넘겨 저장
                request.getTitle(),
                request.getContent(),
                request.getRating()
        );
    }

}
