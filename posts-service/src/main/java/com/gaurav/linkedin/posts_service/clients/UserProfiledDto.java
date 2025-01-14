package com.gaurav.linkedin.posts_service.clients;

import lombok.Data;

import java.time.LocalDate;


@Data
public class UserProfiledDto {
    private String username;


    private String profilePicUrl;


    private LocalDate birthDate;

    private InstituteDto instituteDto;


}
