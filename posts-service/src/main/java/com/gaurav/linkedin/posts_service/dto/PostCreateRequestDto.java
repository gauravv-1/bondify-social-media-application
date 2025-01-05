package com.gaurav.linkedin.posts_service.dto;

import lombok.Data;

@Data
public class PostCreateRequestDto {
    private String content;
    private String imageUrl;

}
