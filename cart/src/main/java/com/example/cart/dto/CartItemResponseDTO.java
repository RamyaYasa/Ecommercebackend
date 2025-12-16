package com.example.cart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {

    private Long id;
    private Long pid;
    private String pname;
    private String description;
    private double price;
    private double actualPrice;
    private int discount;
    private int stockQuantity;
    private int quantity;
    private double totalPrice;
}
