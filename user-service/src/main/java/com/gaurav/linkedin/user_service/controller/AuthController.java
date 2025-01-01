package com.gaurav.linkedin.user_service.controller;


import com.gaurav.linkedin.user_service.dto.LoginRequestDto;
import com.gaurav.linkedin.user_service.dto.SignupRequestDto;
import com.gaurav.linkedin.user_service.dto.UserDto;
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
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        String token = authService.login(loginRequestDto);

        // Store userId in session for subsequent requests
//        session.setAttribute("userId", userId);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully. Session invalidated.");
    }
}
