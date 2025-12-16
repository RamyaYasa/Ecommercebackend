package com.example.cart.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private Long cartId;
    private String userId;
    private List<CartItemResponseDTO> items;
    private double subtotal;
    private double deliveryCharge;
    private double totalPrice;
    private String appliedCoupon;
    private double discountAmount;
}

