package com.gaurav.linkedin.user_service.controller;


import com.gaurav.linkedin.user_service.dto.LoginRequestDto;
import com.gaurav.linkedin.user_service.dto.SignupRequestDto;
import com.gaurav.linkedin.user_service.dto.UserDto;
import com.gaurav.linkedin.user_service.exceptions.ApiResponse;
import com.gaurav.linkedin.user_service.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>("Logged out successfully. Session invalidated."));
    }
}
