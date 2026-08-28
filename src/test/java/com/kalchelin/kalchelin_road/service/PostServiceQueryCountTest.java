package com.kalchelin.kalchelin_road.service;

import com.kalchelin.kalchelin_road.entity.Post;
import com.kalchelin.kalchelin_road.entity.Restaurant;
import com.kalchelin.kalchelin_road.entity.Role;
import com.kalchelin.kalchelin_road.entity.User;
import com.kalchelin.kalchelin_road.dto.PostResponse;
import com.kalchelin.kalchelin_road.repository.PostRepository;
import com.kalchelin.kalchelin_road.repository.RestaurantRepository;
import com.kalchelin.kalchelin_road.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@Transactional
public class PostServiceQueryCountTest {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;
    @Autowired private RestaurantRepository restaurantRepository;

    @Test
    void 글_목록_조회는_작성자가_달라도_쿼리_1번() {
        // Given: 작성자가 서로 다른 글 3개
        for (int i=1; i<=3; i++) {
            User author = userRepository.save(new User("writer" + i, passwordEncoder.encode("pw1234"), Role.USER, "w" + i + "@test.com" ));
            Restaurant restaurant = restaurantRepository.save(new Restaurant(
                    "테스트 가게" + i, "서울 강남구 역삼동 " + i, null,
                    "서울 강남구", 127.000 + i * 0.001, 37.501, null));
            postRepository.save(new Post("제목" + i, "내용", author, 4.5, restaurant));
        }
        em.flush();
        em.clear();     // 캐시를 비워야 진짜 DB 조회가 일어남

        // Hibernate 통계 켜고 카운터 초기화
        Statistics stats = em.getEntityManagerFactory()
                .unwrap(SessionFactory.class).getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        // When: 목록 조회 + DTO 변환(여기서 프록시가 열린다)
        postService.findAll(PageRequest.of(0,10)).map(PostResponse::new);

        // Then: @EntityGraph 덕분에 1번이어야 한다
        // 페이지 크기를 줄이면 count가 추가되어 2가 된다.
        assertThat(stats.getPrepareStatementCount()).isEqualTo(1);
    }
}
