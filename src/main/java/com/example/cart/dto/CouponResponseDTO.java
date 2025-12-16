package com.example.cart.dto;

import com.example.cart.model.CouponType;
import lombok.Data;

@Data
public class CouponResponseDTO {
    private String code;
    private String description;
    private CouponType type;
    private double value;
    private double maxDiscount;
    private boolean active;
}

