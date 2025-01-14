package com.gaurav.linkedin.posts_service.repository;

import com.gaurav.linkedin.posts_service.entity.UserSeenPost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSeenPostRepository extends JpaRepository<UserSeenPost, Long> {
    @Query("SELECT usp.postId FROM UserSeenPost usp WHERE usp.userId = :userId")
    List<Long> findPostIdByUserId(@Param("userId") Long userId);
}
