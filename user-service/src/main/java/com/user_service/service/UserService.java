package com.user_service.service;

import com.user_service.constants.Role;
import com.user_service.dto.*;
import com.user_service.entity.User;
import com.user_service.exception.UserAlreadyExistsException;
import com.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        log.info("Register request received");

        if (request == null) {
            log.error("Register request is null");
            throw new IllegalArgumentException("Request cannot be null");
        }

        String email = request.getEmail().toLowerCase().trim();
        String phone = request.getPhone().trim();

        log.debug("Processing registration for email={} phone={}", email, phone);

        if (userRepository.findByEmailOrPhone(email, phone).isPresent()) {
            log.warn("User already exists with email={} or phone={}", email, phone);
            throw new UserAlreadyExistsException("User already exists with email or phone");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            log.warn("Invalid password for email={}", email);
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        try {
            log.debug("Building user object for email={}", email);

            User user = User.builder()
                    .name(request.getName())
                    .email(email)
                    .phone(phone)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.valueOf(request.getRole())) // enum
                    .createdAt(LocalDateTime.now())
                    .active(true)
                    .build();

            log.debug("Saving user to database email={}", email);

            User savedUser = userRepository.save(user);

            log.info("User registered successfully with id={} email={}",
                    savedUser.getUserId(), email);

            return savedUser;

        } catch (IllegalArgumentException ex) {
            // This will catch Role.valueOf() failure
            log.error("Invalid role provided: {} for email={}", request.getRole(), email, ex);
            throw ex;

        } catch (Exception ex) {
            log.error("Unexpected error while registering user email={}", email, ex);
            throw new RuntimeException("Failed to register user", ex);
        }
    }


    // ✅ GET ALL

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ GET BY ID
    public UserResponse getUserById(Long id) {
        User user = getUser(id);
        return mapToResponse(user);
    }

    // ✅ UPDATE
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = getUser(id);

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        return mapToResponse(userRepository.save(user));
    }

    // ✅ ACTIVATE
    public void activateUser(Long id) {
        User user = getUser(id);
        user.setActive(true);
        userRepository.save(user);
    }

    // ✅ DEACTIVATE
    public void deactivateUser(Long id) {
        User user = getUser(id);
        user.setActive(false);
        userRepository.save(user);
    }

    // 🔥 Helper
    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getUserId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setActive(user.getActive());
        res.setRole(String.valueOf(user.getRole()));
        return res;
    }

}
