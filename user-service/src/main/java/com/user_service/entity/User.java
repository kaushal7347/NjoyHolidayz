package com.user_service.entity;

import com.user_service.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private Boolean active;
}
