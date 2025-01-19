package com.gaurav.linkedin.connection_service.client;


import com.gaurav.linkedin.connection_service.exceptions.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", path = "/users")
public interface UserServiceClient {

    @GetMapping("/profile/getRequestedUsersProfileUrl/{userId}")
    public ApiResponse<String> getRequestedUsersProfileUrl(@PathVariable Long userId);
}
