package com.user_service.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username; //email or phone
    private String password;

}
