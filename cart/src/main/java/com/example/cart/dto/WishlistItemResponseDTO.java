package com.example.cart.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class WishlistItemResponseDTO {

    private Long id;          // DB id
    private UUID itemId;      // business identifier

    private Long pid;
    private String pname;
    private String description;

    private Double price;
    private Double actualPrice;
    private Integer discount;
    private Integer stockQuantity;
}

