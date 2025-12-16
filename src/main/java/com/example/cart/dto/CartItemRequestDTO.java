package com.example.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDTO {

    @NotNull
    private Long pid;

    @NotNull
    private String pname;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Integer stockQuantity;

    private Double actualPrice;

    private Integer discount;

    @NotNull
    @Min(1)
    private Integer quantity;

    private Double rating;
    private String imagePath;
}
