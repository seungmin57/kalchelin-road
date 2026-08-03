package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    @EntityGraph(attributePaths = "author")
    // 몸통은 여전히 Spring Data가 만들어주고, 우리는 "author도 같이 채워와라"만 얹는다.
    Page<Post> findAll(Pageable pageable);
}
