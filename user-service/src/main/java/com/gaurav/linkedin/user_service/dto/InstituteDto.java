package com.gaurav.linkedin.user_service.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class InstituteDto {
    private Long id;

    private String name;

    private String location;
}
