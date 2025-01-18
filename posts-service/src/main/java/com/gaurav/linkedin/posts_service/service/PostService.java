package com.gaurav.linkedin.posts_service.service;

import com.gaurav.linkedin.posts_service.auth.UserContextHolder;
import com.gaurav.linkedin.posts_service.clients.ConnectionsClient;
import com.gaurav.linkedin.posts_service.clients.UserClient;
import com.gaurav.linkedin.posts_service.clients.UserDto;
import com.gaurav.linkedin.posts_service.dto.PersonDto;
import com.gaurav.linkedin.posts_service.dto.PostCreateRequestDto;
import com.gaurav.linkedin.posts_service.dto.PostDto;
import com.gaurav.linkedin.posts_service.entity.Post;
import com.gaurav.linkedin.posts_service.entity.UserSeenPost;
import com.gaurav.linkedin.posts_service.event.PostCreatedEvent;
import com.gaurav.linkedin.posts_service.exceptions.ApiResponse;
import com.gaurav.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.gaurav.linkedin.posts_service.repository.PostRepository;
import com.gaurav.linkedin.posts_service.repository.UserSeenPostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;
    private final JwtService jwtService;
    private final UserSeenPostRepository userSeenPostRepository;
    private final UserClient userClient;

    public PostDto createPost(PostCreateRequestDto postDto,HttpServletRequest request) {
        Long userId = UserContextHolder.getCurrentUserId();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResolutionException("Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7);

        String userName = jwtService.getUserNameFromToken(token);

//        Long userId = jwtService.getUserIdFromToken(token);

        log.info("User Name from Error : {}",userName);
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);
        log.info("Image Urls: {}",postDto.getImageUrl());

        if (post.getImageUrl() == null || post.getImageUrl().isEmpty()) {
            log.info("At if...");
            post.setImageUrl(null);
        }

        post.setProfilePicUrl(postDto.getProfilePicUrl());
        post.setUserName(postDto.getUserName());


        Post savedPost = postRepository.save(post);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .creatorUserName(userName)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic",postCreatedEvent);
        log.info("Saved Post:- {}",savedPost.getImageUrl());
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Retrieving Post with ID: "+postId);


        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not found with Id: "+postId));

        return modelMapper.map(post,PostDto.class);

    }


    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts= postRepository.findByUserId(userId);


        posts.forEach(post -> System.out.println("Post ID: " + post.getId() + ", Image URLs: " + post.getImageUrl()));



        return posts
                .stream()
                .map((element)->modelMapper.map(element,PostDto.class))
                .collect(Collectors.toList());
    }

    public List<PostDto> getAllPostsOfCurrentUser() {
        Long userId = UserContextHolder.getCurrentUserId();
        List<Post> posts= postRepository.findByUserId(userId);

        return posts
                .stream()
                .map((element)->modelMapper.map(element,PostDto.class))
                .collect(Collectors.toList());

    }

    public List<PostDto> getUnseenFeedPosts(int page, int size) {
        Long userId = UserContextHolder.getCurrentUserId();

        // Fetch IDs of posts the user has already seen
        List<Long> seenPostIds = userSeenPostRepository.findPostIdByUserId(userId);

        // Fetch IDs of connected users
        ApiResponse<List<Long>> response = connectionsClient.getConnectedUserId();
        List<Long> connectedUserIds = response.getData(); // Extract the data field
        log.info("Connected User IDs: {}", connectedUserIds);

        // Create a pageable request for pagination
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Fetch unseen posts
        List<Post> unseenPosts = postRepository.findByUserIdInAndIdNotIn(connectedUserIds, seenPostIds, pageable);

        // Map the posts to DTOs and return
        return unseenPosts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    public void markPostAsSeen(List<Long> postIds) {

        Long userId = UserContextHolder.getCurrentUserId();
        List<UserSeenPost> seenPosts = postIds.stream()
                .map(postId -> {
                    UserSeenPost seenPost = new UserSeenPost();
                    seenPost.setUserId(userId);
                    seenPost.setPostId(postId);
                    seenPost.setSeenAt(LocalDateTime.now());
                    return seenPost;
                })
                .collect(Collectors.toList());

        userSeenPostRepository.saveAll(seenPosts);

    }

    public List<PostDto> getSeenFeedPosts(int page, int size) {
        Long userId = UserContextHolder.getCurrentUserId();

        // Fetch IDs of posts the user has already seen
        List<Long> seenPostIds = userSeenPostRepository.findPostIdByUserId(userId);
        log.info("Seen Posts Ids: {}",seenPostIds);

        // Fetch IDs of connected users
        ApiResponse<List<Long>> response = connectionsClient.getConnectedUserId();
        List<Long> connectedUserIds = response.getData(); // Extract the data field
        log.info("Connected User IDs: {}", connectedUserIds);

        // Create a pageable request for pagination
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Fetch seen posts
        List<Post> seenPosts = postRepository.findByUserIdInAndIdIn(connectedUserIds, seenPostIds, pageable);

        // Map the posts to DTOs and return
        return seenPosts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

}
