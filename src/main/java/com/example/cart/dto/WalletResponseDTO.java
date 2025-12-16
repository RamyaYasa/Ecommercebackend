package com.example.cart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {
    private String userId;
    private double balance;
}
