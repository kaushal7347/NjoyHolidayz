package com.user_service.service;

import com.user_service.dto.ForgotPasswordRequest;
import com.user_service.dto.LoginRequest;
import com.user_service.dto.ResetPasswordRequest;
import com.user_service.entity.User;
import com.user_service.repository.UserRepository;
import com.user_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String login(LoginRequest request) {

        String username = request.getUsername();
        User user = userRepository
                .findByEmailOrPhone(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
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
