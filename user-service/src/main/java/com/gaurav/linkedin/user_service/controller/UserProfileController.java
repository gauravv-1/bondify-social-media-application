package com.gaurav.linkedin.user_service.controller;


import com.gaurav.linkedin.user_service.dto.UserProfileUpdateRequest;
import com.gaurav.linkedin.user_service.dto.UserProfiledDto;
import com.gaurav.linkedin.user_service.dto.UserSearchResultDto;
import com.gaurav.linkedin.user_service.entity.UserProfile;
import com.gaurav.linkedin.user_service.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Slf4j
public class UserProfileController {

    @Autowired
    private UserProfileService  userProfileService;

    // Update or create a user profile
    @PostMapping("/completeProfile")
    public ResponseEntity<UserProfiledDto> completeUserProfile(@RequestBody UserProfileUpdateRequest userProfileUpdateRequest, HttpServletRequest httpServletRequest) {
        log.info("At Cont Method complete profile");
        UserProfiledDto userProfiledDto = userProfileService.completeProfile(userProfileUpdateRequest,httpServletRequest);
        return new ResponseEntity<>(userProfiledDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResultDto>> searchUsers(@RequestParam(required = false) String searchTerm) {
        System.out.println("Search Term: " + searchTerm); // Log search term
        List<UserSearchResultDto> userSearchResultDto = userProfileService.searchUser(searchTerm);
        return new ResponseEntity<>(userSearchResultDto, HttpStatus.OK);
    }

}
