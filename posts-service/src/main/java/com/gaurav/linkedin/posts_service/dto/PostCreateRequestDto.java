package com.gaurav.linkedin.posts_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequestDto {
    private String content;
    private List<String> imageUrl;
    private String profilePicUrl;
    private String userName;

}
