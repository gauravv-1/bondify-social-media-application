package com.gaurav.linkedin.user_service.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfiledDto {
    private String username;


    private String profilePicUrl;


    private LocalDate birthDate;

    private InstituteDto instituteDto;
}
