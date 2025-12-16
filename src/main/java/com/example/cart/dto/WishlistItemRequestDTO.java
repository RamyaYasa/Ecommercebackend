package com.example.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WishlistItemRequestDTO {

    @NotNull
    private Long pid;

    @NotNull
    private String pname;

    private String description;

    @NotNull
    private Double price;

    private Double actualPrice;
    private Integer discount;
    private Integer stockQuantity;
    private Double rating;
    private String imagePath;
}

