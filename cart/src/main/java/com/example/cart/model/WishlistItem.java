package com.example.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID itemId;

    private Long pid;
    private String pname;
    private String description;
    private Double price;
    private Double actualPrice;
    private Integer discount;
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    @JsonIgnore   // <<<<<<<< ADD THIS
    private Wishlist wishlist;
}
