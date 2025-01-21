package com.gaurav.linkedin.posts_service.dto;

import com.gaurav.linkedin.posts_service.entity.PostType;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {

    private Long id;
    private String content;
    private List<String> imageUrl;
    private Long userId;
    private LocalDateTime createdAt;
    private String profilePicUrl;
    private String userName;

    private Long instituteId;
    private PostType postType;

}
