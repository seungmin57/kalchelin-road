package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.dto.RestaurantRequest;
import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.Restaurant;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.EmailNotVerifiedException;
import com.kalchelin.kalchelin_road.exception.ResourceNotFoundException;
import com.kalchelin.kalchelin_road.repository.PostRepository;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;

    // 글 작성 - author는 컨트롤러가 세션에서 꺼내 넘겨준다

    @Transactional
    public Post create(String title, String content, User sessionUser, Double rating,
                       RestaurantRequest restaurantRequest) {
        // 세션 User는 로그인 순간의 스냅샷. 인증 여부는 DB의 현재 값으로 판단한다
        User author = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        if (!author.isEmailVerified()) {
            throw new EmailNotVerifiedException("이메일 인증 후 글을 작성할 수 있습니다.");
        }

        // 인증 검사를 통과한 뒤에 가게를 만든다
        Restaurant restaurant = restaurantService.findOrCreate(restaurantRequest);

        Post post = new Post(title, content, author, rating, restaurant);
        return postRepository.save(post);
    }

    // 목록 조회
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다. id =" + id));
    }

    // 수정 - 본인 글만
    @Transactional
    public Post update(Long id, String title, String content, User currentUser, Double rating) {
        Post post = findById(id);       // 있는지 확인(없으면 예외)
        checkOwner(post, currentUser);  // 소유권 검사
        post.update(title, content, rating);    // 엔티티가 자기 상태를 바꿈
        return post;    // save 필요 없음

    }

    // 삭제 - 본인 글만
    @Transactional
    public void delete(Long id, User currentUser) {
        Post post = findById(id);
        checkOwner(post, currentUser);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> findByRestaurant(Long restaurantId, Pageable pageable) {
        return postRepository.findByRestaurantId(restaurantId, pageable);
    }

    // 소유권 검사
    private void checkOwner(Post post, User currentUser) {
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("본인 글만 수정/삭제할 수 있습니다.");
        }
    }

}
