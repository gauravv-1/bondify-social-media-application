package com.gaurav.linkedin.user_service.service;

import com.gaurav.linkedin.user_service.auth.UserContextHolder;
import com.gaurav.linkedin.user_service.dto.InstituteDto;
import com.gaurav.linkedin.user_service.dto.UserProfileUpdateRequest;
import com.gaurav.linkedin.user_service.dto.UserProfiledDto;
import com.gaurav.linkedin.user_service.dto.UserSearchResultDto;
import com.gaurav.linkedin.user_service.entity.Institute;
import com.gaurav.linkedin.user_service.entity.User;
import com.gaurav.linkedin.user_service.entity.UserProfile;
import com.gaurav.linkedin.user_service.event.ProfileUpdatedEvent;
import com.gaurav.linkedin.user_service.repository.InstituteRepository;
import com.gaurav.linkedin.user_service.repository.UserRepository;
import com.gaurav.linkedin.user_service.repository.UserProfileRepository;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {


    private final UserRepository userRepository;

    private final JwtService jwtService;


    private final UserProfileRepository userProfileRepository;


    private final InstituteRepository  instituteRepository;


    private final ModelMapper modelMapper;

    private final KafkaTemplate<Long, ProfileUpdatedEvent> kafkaTemplate;

    @Transactional
    public UserProfiledDto completeProfile(UserProfileUpdateRequest userProfileUpdateRequest, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResolutionException("Authorization header is missing or invalid");
        }
        String token = authorizationHeader.substring(7);


        Long userId = jwtService.getUserIdFromToken(token);
        log.info("User Id: {}", userId);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResolutionException("User not found"));

        // Create or update the user profile
        UserProfile userProfile = new UserProfile();
        ProfileUpdatedEvent profileUpdatedEvent = new ProfileUpdatedEvent();

        // Set the username
        userProfile.setUsername(userProfileUpdateRequest.getUsername());

        // Handle institute selection
        if (userProfileUpdateRequest.getInstituteDto() != null) {
            InstituteDto instituteDto = userProfileUpdateRequest.getInstituteDto();
            Institute institute = modelMapper.map(instituteDto, Institute.class);
            userProfile.setInstitute(institute);
            profileUpdatedEvent.setInstituteId(institute.getId());
            instituteRepository.save(institute);
            log.info("Institute saved");
        }

        // Set additional profile details
        userProfile.setProfilePicUrl(userProfileUpdateRequest.getProfilePicUrl());
        userProfile.setBirthDate(userProfileUpdateRequest.getBirthDate());
        userProfile.setUser(user);

        // Publish Kafka topic
        profileUpdatedEvent.setUserId(userId);
        kafkaTemplate.send("profile-updated-topic", profileUpdatedEvent);
        user.setIsProfileComplete(true);
        userRepository.save(user);

        // Save the user profile
        userProfileRepository.save(userProfile);

        // Map and return the response DTO
        return modelMapper.map(userProfile, UserProfiledDto.class);
    }

    public List<UserSearchResultDto> searchUser(String searchTerm) {
        System.out.println("Searching for: " + searchTerm);  // Log the search term
        List<UserSearchResultDto> result = userRepository.searchUsers(searchTerm);
        System.out.println("Query Result: " + result); // Log the result
        return result;
    }
}
