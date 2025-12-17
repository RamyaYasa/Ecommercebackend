package com.ecommerce.backend.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long pid;
    private String pname;
    private String description;
    private double price;
    private int stockQuantity;
    private String imagePath;
    private double actualPrice;
    private int discount;
    private double rating;
    private int Quantity;
    private CategoryDTO category;
}
