package com.login.auth.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
}
