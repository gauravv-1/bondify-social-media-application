package com.gaurav.linkedin.posts_service.repository;

import com.gaurav.linkedin.posts_service.entity.Post;
import org.springframework.data.domain.Pageable; // Correct import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.userId IN :userIds AND p.id NOT IN :excludedPostIds")
    List<Post> findByUserIdInAndIdNotIn(
            @Param("userIds") List<Long> userIds,
            @Param("excludedPostIds") List<Long> excludedPostIds,
            Pageable pageable); // Correct Pageable type
}
