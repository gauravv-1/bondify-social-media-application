package com.gaurav.linkedin.posts_service.service;

import com.gaurav.linkedin.posts_service.auth.UserContextHolder;
import com.gaurav.linkedin.posts_service.entity.Post;
import com.gaurav.linkedin.posts_service.entity.PostLike;
import com.gaurav.linkedin.posts_service.event.PostLikedEvent;
import com.gaurav.linkedin.posts_service.exceptions.BadRequestException;
import com.gaurav.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.gaurav.linkedin.posts_service.repository.PostLikeRepository;
import com.gaurav.linkedin.posts_service.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long,PostLikedEvent> kafkaTemplate;
    private final JwtService jwtService;

    public void likePost(Long postId, HttpServletRequest request){

        Long userId = UserContextHolder.getCurrentUserId();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResolutionException("Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7);

        String userName = jwtService.getUserNameFromToken(token);

        log.info("Attempting to like the post with id:{}",postId);

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post not found with Id: "+postId));



        boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if (isLiked) throw new BadRequestException("Cannot like the same Post again");

        PostLike postLike  = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        log.info("Post with id :{} liked successfully",postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .likedByUserName(userName)
                .build();

        kafkaTemplate.send("post-liked-topic",postId,postLikedEvent);




    }

    public void unlikePost(Long postId) {

        Long userId = UserContextHolder.getCurrentUserId();

        log.info("Attempting to unlike the post with id:{}",postId);

        boolean isExits = postRepository.existsById(postId);
        if(!isExits) throw new ResourceNotFoundException("Post not found with Id: "+postId);

        boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if (!isLiked) throw new BadRequestException("Cannot unlike the same Post which is not liked");


        postLikeRepository.deleteByUserIdAndPostId(userId,postId);

        log.info("Post with id :{} unliked successfully",postId);


    }
}
