package com.example.cart.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WishlistResponseDTO {

    private UUID wishlistId;
    private String userId;

    private List<WishlistItemResponseDTO> items;
}
