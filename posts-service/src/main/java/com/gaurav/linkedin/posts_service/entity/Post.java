package com.gaurav.linkedin.posts_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_image_url", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imageUrl;

    @Column(nullable = true)
    private String profilePicUrl;
    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private Long instituteId;  // Institute ID for institute-specific posts

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;  // Type of post: NORMAL or INSTITUTE


}
