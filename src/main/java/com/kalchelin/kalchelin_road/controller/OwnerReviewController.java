package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.dto.OwnerReviewRequest; // 데이터 요청 그릇 가져오기
import com.kalchelin.kalchelin_road.entity.OwnerReview;     // 돌려줄 데이터(엔티티) 가져오기
import com.kalchelin.kalchelin_road.service.OwnerReviewService;  // 일을 시킬 대상(서비스) 가져오기
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // [상세 조회] GET  /api/owner-reviews/id
    @GetMapping("/{id}")     // 주소 끝의 {id} 자리에 들어온 값을 받겠다는 뜻
    public OwnerReview getReview(@PathVariable Long id) {
        // @PathVariable = 주소에 있는 {id} 값을 꺼내서 이 매개변수 id에 담아라
        return ownerReviewService.getReview(id);
    }

    // [수정] PUT /api/owner-reviews/id
    @PutMapping("/{id}")    // PUT = 기존 것을 수정할 때 쓰는 요청 방식
    public OwnerReview updateReview(@PathVariable Long id, @RequestBody OwnerReviewRequest request) {
        // 주소에서 id를 꺼내고(@PathVariable), 바꿀 내용은 본문 JSON에서 꺼냄(@RequestBody)
        return ownerReviewService.updateReview(id, request.getTitle(), request.getContent(), request.getRating());
    }

    // [삭제] DELETE /api/owner-reviews/id
    @DeleteMapping("/{id}")     // DELETE = 삭제할 때 쓰는 요청 방식
    public void deleteReview(@PathVariable Long id) {
        ownerReviewService.deleteReview(id);
    }

    // [이미지 업로드] POST /api/owner-reviews/{id}/image
    @PostMapping("/{id}/image")     //특정 평가(id) 밑의 image라는 하위 주소로 POST
    public OwnerReview uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        // @PathVariable = 주소의 {id} 값을 꺼냄 / @RequestParam('image') = form-data의 image부분(파일)을 꺼냄
        return ownerReviewService.updateImage(id, image);
    }
}
