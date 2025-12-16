package com.login.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String phone;
    private String email;
    private String password;

    private String otp;
    private Long otpExpires;

    @Enumerated(EnumType.STRING)
    private Role role;
}
