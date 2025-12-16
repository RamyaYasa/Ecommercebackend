package com.example.cart.service;

import com.example.cart.dto.WalletResponseDTO;

public interface WalletService {

    WalletResponseDTO getWallet(String userId);

    WalletResponseDTO addMoney(String userId, double amount);

    WalletResponseDTO deductMoney(String userId, double amount);
}

