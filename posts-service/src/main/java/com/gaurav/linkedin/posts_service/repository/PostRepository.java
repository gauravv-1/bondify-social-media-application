package com.gaurav.linkedin.posts_service.repository;

import com.gaurav.linkedin.posts_service.entity.Post;
import com.gaurav.linkedin.posts_service.entity.PostType;
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

    @Query("SELECT p FROM Post p WHERE p.userId IN :userIds AND p.id IN :postIds")
    List<Post> findByUserIdInAndIdIn(@Param("userIds") List<Long> userIds, @Param("postIds") List<Long> postIds, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.instituteId = :instituteId AND p.postType = :postType AND p.id NOT IN :excludedPostIds")
    List<Post> findByInstituteIdAndPostTypeAndIdNotIn(
            @Param("instituteId") Long instituteId,
            @Param("postType") PostType postType,
            @Param("excludedPostIds") List<Long> excludedPostIds,
            Pageable pageable);

    @Query("SELECT p FROM Post p WHERE " +
            "(p.postType = 'NORMAL' OR (p.postType = 'INSTITUTE' AND p.instituteId = :instituteId)) " +
            "AND p.userId IN :connectedUserIds " +
            "AND p.id NOT IN :excludedPostIds")
    List<Post> findUnseenNormalPosts(
            @Param("connectedUserIds") List<Long> connectedUserIds,
            @Param("excludedPostIds") List<Long> excludedPostIds,
            @Param("instituteId") Long instituteId,
            Pageable pageable);


    @Query("SELECT p FROM Post p WHERE " +
            "p.postType = 'NORMAL' " +
            "AND p.userId IN :connectedUserIds " +
            "AND p.id NOT IN :excludedPostIds")
    List<Post> findUnseenNormalPostsOnly(
            @Param("connectedUserIds") List<Long> connectedUserIds,
            @Param("excludedPostIds") List<Long> excludedPostIds,
            Pageable pageable);


    @Query("SELECT p FROM Post p WHERE " +
            "p.postType = 'INSTITUTE' " +
            "AND p.instituteId = :instituteId " +
            "AND p.userId IN :connectedUserIds " +
            "AND p.id NOT IN :excludedPostIds")
    List<Post> findUnseenInstitutePosts(
            @Param("connectedUserIds") List<Long> connectedUserIds,
            @Param("instituteId") Long instituteId,
            @Param("excludedPostIds") List<Long> excludedPostIds,
            Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.userId IN :connectedUserIds AND p.id IN :seenPostIds AND p.postType = :postType")
    List<Post> findByUserIdInAndIdInAndPostType(
            @Param("connectedUserIds") List<Long> connectedUserIds,
            @Param("seenPostIds") List<Long> seenPostIds,
            @Param("postType") PostType postType,
            Pageable pageable
    );





}
