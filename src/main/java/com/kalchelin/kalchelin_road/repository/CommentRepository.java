package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 글의 댓글만 조회 - 메서드 이름으로 쿼리
    @EntityGraph(attributePaths = "author")
    List<Comment> findByPostId(Long postId);
}
