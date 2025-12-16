package com.login.auth.service;

import com.login.auth.dto.*;
import com.login.auth.entity.Role;
import com.login.auth.entity.User;
import com.login.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final JwtService jwt;

    private final BCryptPasswordEncoder encoder;

    public String signup(SignupRequest req) {

        if (!req.getPassword().equals(req.getConfirmPassword()))
            throw new RuntimeException("Passwords do not match");

        if (repo.findByEmail(req.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        User user = User.builder()
                .name(req.getName())
                .phone(req.getPhone())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(Role.ROLE_USER) //Default role
                .build();

        repo.save(user);

        return "User created successfully";
    }

    public LoginResponse login(LoginRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        String accessToken = jwt.generateAccessToken(user);
        String refreshToken = jwt.generateRefreshToken(user);


        return new LoginResponse(accessToken, refreshToken);
    }

}
