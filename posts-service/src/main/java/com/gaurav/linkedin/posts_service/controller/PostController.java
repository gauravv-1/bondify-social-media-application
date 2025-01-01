package com.gaurav.linkedin.posts_service.controller;

import com.gaurav.linkedin.posts_service.auth.UserContextHolder;
import com.gaurav.linkedin.posts_service.dto.PostCreateRequestDto;
import com.gaurav.linkedin.posts_service.dto.PostDto;
import com.gaurav.linkedin.posts_service.entity.Post;
import com.gaurav.linkedin.posts_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto){
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId,HttpServletRequest httpServletRequest){

        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }



    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId){
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }





}
