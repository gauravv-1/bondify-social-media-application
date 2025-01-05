package com.gaurav.linkedin.posts_service.controller;

import com.gaurav.linkedin.posts_service.dto.PostDto;
import com.gaurav.linkedin.posts_service.exceptions.ApiResponse;
import com.gaurav.linkedin.posts_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> likePost(@PathVariable Long postId){
        postLikeService.likePost(postId);
        return ResponseEntity.ok(new ApiResponse<>(null));

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> unlikePost(@PathVariable Long postId){
        postLikeService.unlikePost(postId);
        return ResponseEntity.ok(new ApiResponse<>(null));

    }
}
