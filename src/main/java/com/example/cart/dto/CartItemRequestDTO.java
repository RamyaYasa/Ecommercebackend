package com.example.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDTO {

    @NotNull
    private Long pid;  // ONLY product ID

    @NotNull
    @Min(1)
    private Integer quantity;  // ONLY quantity


    // private String pname;
    // private String description;
    // private Double price;
    // private Integer stockQuantity;
    // private Double actualPrice;
    // private Integer discount;
    // private Double rating;
    // private String imagePath;
}