package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.dto.PostRequest;
import com.kalchelin.kalchelin_road.dto.PostResponse;
import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.service.CustomUserDetails;
import com.kalchelin.kalchelin_road.service.PostService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 글 작성: POST /api/posts (로그인한 회원만)
    @PostMapping
    public PostResponse create(@Valid @RequestBody PostRequest request,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {  // 지금 로그인한 사람
        Post post = postService.create(request.getTitle(), request.getContent(), userDetails.getUser(), request.getRating());
        return new PostResponse(post);
    }

    // 목록 조회: GET /api/posts (누구나)
    @GetMapping
    public List<PostResponse> findAll() {
        return postService.findAll().stream()
                .map(PostResponse::new)     // Post 하나하나를 PostResponse로
                .toList();
    }


    // 상세 조회: GET /api/posts/{id} (누구나)
    @GetMapping("/{id}")
    public PostResponse findOne(@PathVariable Long id) {
        Post post = postService.findById(id);
        return new PostResponse(post);
    }

    // 수정: PUT /api/posts/{id}  (본인만)
    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Post post = postService.update(id, request.getTitle(), request.getContent(), userDetails.getUser(), request.getRating());
        return new PostResponse(post);
    }

    // 삭제: DELETE /api/posts/{id}  (본인만)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.delete(id, userDetails.getUser());
    }
}
