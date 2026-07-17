package com.kalchelin.kalchelin_road.repository;

import com.kalchelin.kalchelin_road.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
