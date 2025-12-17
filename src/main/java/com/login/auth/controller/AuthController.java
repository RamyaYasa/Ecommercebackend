package com.login.auth.controller;

import com.login.auth.dto.*;
import com.login.auth.entity.User;
import com.login.auth.repository.UserRepository;
import com.login.auth.service.AuthService;
import com.login.auth.service.JwtService;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final JwtService jwtService;

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) {
        return service.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

//    @GetMapping("/validate")
//    public ResponseEntity<Boolean> validateToken(
//            @RequestHeader("Authorization") String authorizationHeader
//    ) {
//        String token = authorizationHeader.replace("Bearer ", "");
//
//
//        String userId = jwtService.extractUserId(token);
//
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 3. Validate token
//        boolean isValid = jwtService.validateToken(token, user);
//
//        return ResponseEntity.ok(isValid);
//    }

    @GetMapping("/extract/claims")
    public ResponseEntity<String> extractClaims(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        String token = authorizationHeader.replace("Bearer ", "");

        String userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(userId);
    }







}
