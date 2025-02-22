package com.gaurav.linkedin.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSearchResultDto {
    private Long userId;
    private String name;
    private String username;
    private String profilePicUrl;
    private String institute;

    // Constructor matching the fields used in the query
    public UserSearchResultDto(String name, String username, String profilePicUrl,String institute) {
        this.name = name;
        this.username = username;
        this.profilePicUrl = profilePicUrl;
        this.institute = institute;
    }
}
