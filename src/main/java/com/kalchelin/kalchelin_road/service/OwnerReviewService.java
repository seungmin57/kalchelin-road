package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.OwnerReview;     //다룰 데이터(엔티티) 가져오기
import com.kalchelin.kalchelin_road.repository.OwnerReviewRepository;        //DB 접근 도구(리포지토리) 가져오기
import org.springframework.stereotype.Service;        //@Service 어노테이션 가져오기
import java.util.NoSuchElementException;

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

    // [기능 3] 상세 조회 - id로 평가 하나 찾기
    public OwnerReview getReview(Long id) {
        return ownerReviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 평가가 없습니다. id =" + id));
        // id로 조회(결과가 없을 수도 있어서 Optional로 감싸져 옴)
        // orElseThrow = 값이 있으면 꺼내고, 없으면 에러를 던져라
    }

    // [기능 4] 수정 - id로 찾아서 내용 바꾸기
    public OwnerReview updateReview(Long id, String title, String content, double rating) {
        OwnerReview review = getReview(id);     // 상세 조회 메서드 사용
        review.update(title, content, rating);  // 엔티티 안의 값을 수정 - 엔티티에 메서드 추가
        return ownerReviewRepository.save(review);  // 바뀐 걸 저장
    }

    // [기능 5] id로 평가 삭제
    public void deleteReview(Long id) {
        ownerReviewRepository.deleteById(id);       // id로 삭제
    }
}
