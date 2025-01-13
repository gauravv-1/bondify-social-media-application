package com.gaurav.linkedin.user_service.config;

import com.gaurav.linkedin.user_service.dto.InstituteDto;
import com.gaurav.linkedin.user_service.dto.UserDto;
import com.gaurav.linkedin.user_service.dto.UserProfiledDto;
import com.gaurav.linkedin.user_service.entity.Institute;
import com.gaurav.linkedin.user_service.entity.User;
import com.gaurav.linkedin.user_service.entity.UserProfile;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Map User to UserDto
        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map(source.getUserProfile(), destination.getUserProfiledDto());
            }
        });

        // Map UserProfile to UserProfiledDto
        modelMapper.addMappings(new PropertyMap<UserProfile, UserProfiledDto>() {
            @Override
            protected void configure() {
                map(source.getInstitute(), destination.getInstituteDto());
            }
        });

        // Map Institute to InstituteDto
        modelMapper.addMappings(new PropertyMap<Institute, InstituteDto>() {
            @Override
            protected void configure() {
                // Default property mapping (if field names match)
            }
        });

        return modelMapper;
    }
}
