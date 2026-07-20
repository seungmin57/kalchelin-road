package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.exception.ResourceNotFoundException;
import com.kalchelin.kalchelin_road.repository.PostRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 글 작성 - author는 컨트롤러가 세션에서 꺼내 넘겨준다
    public Post create(String title, String content, User author) {
        Post post = new Post(title, content, author);
        return postRepository.save(post);
    }

    // 목록 조회
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    // 상세 조회
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다. id =" + id));
    }

    // 수정 - 본인 글만
    @Transactional
    public Post update(Long id, String title, String content, User currentUser) {
        Post post = findById(id);       // 있는지 확인(없으면 예외)
        checkOwner(post, currentUser);  // 소유권 검사
        post.update(title, content);    // 엔티티가 자기 상태를 바꿈
        return post;    // save 필요 없음

    }

    // 삭제 - 본인 글만
    public void delete(Long id, User currentUser) {
        Post post = findById(id);
        checkOwner(post, currentUser);
        postRepository.delete(post);
    }

    // 소유권 검사
    private void checkOwner(Post post, User currentUser) {
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("본인 글만 수정/삭제할 수 있습니다.");
        }
    }

}
