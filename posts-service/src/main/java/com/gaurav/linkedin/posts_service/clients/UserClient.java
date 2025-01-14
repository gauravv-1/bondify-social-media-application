package com.gaurav.linkedin.posts_service.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service",path = "/users")
public interface UserClient {

    @GetMapping("/getUserById/{userId}")
    public UserDto getUserById(@PathVariable Long userId);

}
