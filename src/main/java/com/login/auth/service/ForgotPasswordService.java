package com.login.auth.service;

import com.login.auth.dto.*;
import com.login.auth.entity.User;
import com.login.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository repo;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder encoder;

    public String sendOtp(ForgotPasswordRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        user.setOtp(otp);
        user.setOtpExpires(System.currentTimeMillis() + 5 * 60 * 1000);
        repo.save(user);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Your OTP");
        msg.setText("Your OTP is: " + otp);

        mailSender.send(msg);

        return "OTP sent successfully";
    }

    public String verifyOtp(VerifyOtpRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(req.getOtp()))
            throw new RuntimeException("Invalid OTP");

        if (System.currentTimeMillis() > user.getOtpExpires())
            throw new RuntimeException("OTP expired");

        return "OTP verified";
    }

    public String resetPassword(ResetPasswordRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpires(null);

        repo.save(user);

        return "Password reset successfully";
    }
}
