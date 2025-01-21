package com.gaurav.linkedin.posts_service.dto;

import com.gaurav.linkedin.posts_service.entity.PostType;
import lombok.Data;

@Data
public class PersonDto {
    private Long id;
    private Long userId;
    private String name;

}
