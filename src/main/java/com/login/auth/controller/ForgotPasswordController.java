package com.login.auth.controller;

import com.login.auth.dto.*;
import com.login.auth.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService service;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody ForgotPasswordRequest req) {
        return service.sendOtp(req);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest req) {
        return service.verifyOtp(req);
    }

    @PostMapping("/reset")
    public String reset(@RequestBody ResetPasswordRequest req) {
        return service.resetPassword(req);
    }
}
