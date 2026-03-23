package com.user_service.controller;

import com.user_service.dto.ForgotPasswordRequest;
import com.user_service.dto.LoginRequest;
import com.user_service.dto.RegisterRequest;
import com.user_service.dto.ResetPasswordRequest;
import com.user_service.service.AuthService;
import com.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000" )
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("Reset token sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

}
