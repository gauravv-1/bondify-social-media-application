package com.gaurav.linkedin.posts_service.clients;


import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;
    private String email;
    private Boolean isProfileComplete;
    private UserProfiledDto userProfiledDto;
}
