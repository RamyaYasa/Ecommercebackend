package com.example.cart.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    private String code; // Coupon code (PRIMARY KEY)

    private String description;

    // Types: PERCENTAGE, FLAT, FREESHIP
    @Enumerated(EnumType.STRING)
    private CouponType type;

    private double value;      // % or flat discount
    private double maxDiscount; // applicable only for percentage coupons

    private boolean active = true;
}

