package com.user_service.service;

import com.user_service.dto.*;
import com.user_service.entity.User;
import com.user_service.repository.UserRepository;
import com.user_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {

        String username = request.getUsername();
        User user = userRepository
                .findByEmailOrPhone(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);

        AuthResponse response = new AuthResponse(
                token,
                "Bearer",
                86400000L,
                new UserResponse(user.getUserId(), user.getName(), user.getEmail(), user.getRole(), user.getActive())
        );

        return response;
    }

    public void forgotPassword(ForgotPasswordRequest request) {

        String username = request.getUsername();
        System.out.println(username);
        User user = userRepository
                .findByEmailOrPhone(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate token
        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15)); // 15 mins expiry

        userRepository.save(user);

        // TODO: Send token via email/SMS
        System.out.println("Reset Token: " + token);
        emailService.sendResetEmail(user.getEmail(), token);
    }

    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository
                .findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Clear token after use
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

}
