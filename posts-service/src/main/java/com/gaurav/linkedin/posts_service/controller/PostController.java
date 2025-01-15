package com.gaurav.linkedin.posts_service.controller;

import com.gaurav.linkedin.posts_service.auth.UserContextHolder;
import com.gaurav.linkedin.posts_service.dto.PostCreateRequestDto;
import com.gaurav.linkedin.posts_service.dto.PostDto;
import com.gaurav.linkedin.posts_service.entity.Post;
import com.gaurav.linkedin.posts_service.exceptions.ApiResponse;
import com.gaurav.linkedin.posts_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto, HttpServletRequest request){
        log.info("At PostController createPost method");
        postDto.getImageUrl().forEach((e)->log.info(e," Img Urls"));
        PostDto createdPost = postService.createPost(postDto,request);
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

    @GetMapping("/users/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfCurrentUser(){
        List<PostDto> posts = postService.getAllPostsOfCurrentUser();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed/getUnseenPosts")
    public ResponseEntity<List<PostDto>> getUnseenPosts(@RequestParam int page, @RequestParam int size) {
        List<PostDto> postDto = postService.getUnseenPosts(page,size);



        return ResponseEntity.ok(postDto);
    }


    @PostMapping("/feed/mark-seen")
    public ResponseEntity<ApiResponse<Boolean>> markPostsAsSeen(@RequestBody List<Long> postIds) {
        postService.markPostAsSeen(postIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }







}
