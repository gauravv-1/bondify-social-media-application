package com.gaurav.linkedin.user_service.controller;


import com.gaurav.linkedin.user_service.dto.LoginRequestDto;
import com.gaurav.linkedin.user_service.dto.SignupRequestDto;
import com.gaurav.linkedin.user_service.dto.UserDto;
import com.gaurav.linkedin.user_service.exceptions.ApiResponse;
import com.gaurav.linkedin.user_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        UserDto userDto = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        String token = authService.login(loginRequestDto);

        // Store userId in session for subsequent requests
//        session.setAttribute("userId", userId);
        return ResponseEntity.ok(new ApiResponse<>(token));
    }

    @GetMapping("/getUserProfile")
    public ResponseEntity<UserDto> getUserProfile(HttpServletRequest httpServletRequest){
        log.info("At getUserProfile AuthController");
        UserDto userDto = authService.getUserProfile(httpServletRequest);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>("Logged out successfully. Session invalidated."));
    }

    @PostMapping("/{userId}/getRequestedUsersProfile")
    public ResponseEntity<UserDto> getRequestedUsersProfile(@PathVariable Long userId){
        log.info("At getRequestedUsersProfile with userId : {}",userId);

        return new ResponseEntity<>(authService.getRequestedUsersProfile(userId),HttpStatus.OK);
    }

    @GetMapping("/getUserById/{userId}")
    public UserDto getUserById(@PathVariable Long userId){
        log.info("At getUserById auth controller..");
        log.info("At getUserById auth controller..");
        log.info("At getUserById auth controller..");
        return authService.getUserById(userId);
    }

}
