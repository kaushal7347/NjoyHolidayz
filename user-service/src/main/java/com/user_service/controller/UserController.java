package com.user_service.controller;

import com.user_service.dto.UpdateUserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entity.User;
import com.user_service.repository.UserRepository;
import com.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000" )
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

        private final UserService userService;

        // ✅ GET ALL USERS
        @GetMapping("/users")
        public List<UserResponse> getAllUsers() {
            return userService.getAllUsers();
        }

        // ✅ GET USER BY ID
        @GetMapping("/user/{id}")
        public UserResponse getUserById(@PathVariable Long id) {
            return userService.getUserById(id);
        }

        // ✅ UPDATE USER
        @PutMapping("/user/{id}")
        public UserResponse updateUser(@PathVariable Long id,
                                       @RequestBody UpdateUserRequest request) {
            return userService.updateUser(id, request);
        }

        // ✅ ACTIVATE USER
        @PatchMapping("/activate/{id}")
        public String activateUser(@PathVariable Long id) {
            userService.activateUser(id);
            return "User activated";
        }

        // ✅ DEACTIVATE USER
        @PatchMapping("/deactivate/{id}")
        public String deactivateUser(@PathVariable Long id) {
            userService.deactivateUser(id);
            return "User deactivated";
        }


}
