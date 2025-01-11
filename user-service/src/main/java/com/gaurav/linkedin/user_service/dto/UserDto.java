package com.gaurav.linkedin.user_service.dto;


import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;
    private String email;
    private Boolean isProfileComplete;

}
