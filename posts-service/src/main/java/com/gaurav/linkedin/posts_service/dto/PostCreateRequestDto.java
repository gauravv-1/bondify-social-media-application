package com.gaurav.linkedin.posts_service.dto;

import com.gaurav.linkedin.posts_service.entity.PostType;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequestDto {
    private String content;
    private List<String> imageUrl;
    private String profilePicUrl;
    private String userName;
    private PostType postType;

}
