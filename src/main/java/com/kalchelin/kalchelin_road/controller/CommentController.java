package com.kalchelin.kalchelin_road.controller;

import com.kalchelin.kalchelin_road.dto.CommentRequest;
import com.kalchelin.kalchelin_road.dto.CommentResponse;
import com.kalchelin.kalchelin_road.service.CommentService;
import com.kalchelin.kalchelin_road.service.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성: POST  /api/posts/1/comments
    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
                var comment = commentService.create(postId, request.getContent(), userDetails.getUser());
                return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse(comment));
    }

    // 댓글 목록: GET   /api/posts/1/comments
    @GetMapping
    public List<CommentResponse> findByPost(@PathVariable("postId") Long postId) {
        return commentService.findByPost(postId).stream().map(CommentResponse::new).toList();
    }

    // 댓글 삭제: DELETE    /api/posts/1/comments/5
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.delete(commentId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
