package com.gaurav.linkedin.posts_service.clients;

import com.gaurav.linkedin.posts_service.exceptions.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service",path = "/users")
public interface UserClient {

    @GetMapping("/getUserById/{userId}")
    public UserDto getUserById(@PathVariable Long userId);

    @GetMapping("/profile/getRequestedUsersProfileUrl/{userId}")
    public ApiResponse<String> getRequestedUsersProfileUrl(@PathVariable Long userId);


    @PostMapping("/auth/{userId}/getRequestedUsersProfile")
    public ApiResponse<UserDto> getRequestedUsersProfile(@PathVariable Long userId);

}
