package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.Comment;
import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.EmailNotVerifiedException;
import com.kalchelin.kalchelin_road.exception.ResourceNotFoundException;
import com.kalchelin.kalchelin_road.repository.CommentRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;      // 글이 존재하는지 확인하려고 재사용

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    // 댓글 작성
    public Comment create(Long postId, String content, User author) {
        if (!author.isEmailVerified()) {
            throw new EmailNotVerifiedException("이메일 인증 후 댓글을 작성할 수 있습니다.");
        }
        Post post = postService.findById(postId);   // 글이 있는지 확인(없으면 예외)
        Comment comment = new Comment(content, post, author);
        return commentRepository.save(comment);
    }

    // 특정 글의 댓글 목록
    public List<Comment> findByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 삭제 - 본인 댓글만 (소유권 검사, 글에서 배운 것 재사용)
    public void delete(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다."));
        if(!comment.getAuthor().getId().equals(currentUser.getId()))
            throw new AccessDeniedException("본인 댓글만 삭제할 수 있습니다.");
        commentRepository.delete(comment);
    }

}
