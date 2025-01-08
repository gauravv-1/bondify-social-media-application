package com.gaurav.linkedin.user_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequest {
    private String username;


    private String profilePicUrl;


    private LocalDate birthDate;

    private InstituteDto instituteDto;
}
