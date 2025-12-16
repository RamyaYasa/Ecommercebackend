package com.example.cart.service;

import com.example.cart.dto.WalletResponseDTO;
import com.example.cart.model.Wallet;
import com.example.cart.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    private Wallet getOrCreateWallet(String userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet wallet = Wallet.builder()
                    .userId(userId)
                    .balance(0)
                    .updatedAt(LocalDateTime.now())
                    .build();
            return walletRepository.save(wallet);
        });
    }
    @Override
    public WalletResponseDTO getWallet(String userId) {
        Wallet wallet = getOrCreateWallet(userId);
        return new WalletResponseDTO(userId, wallet.getBalance());
    }

    @Override
    @Transactional
    public WalletResponseDTO addMoney(String userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
        Wallet wallet = getOrCreateWallet(userId);
        wallet.setBalance(wallet.getBalance() + amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
        return new WalletResponseDTO(userId, wallet.getBalance());
    }
    @Override
    @Transactional
    public WalletResponseDTO deductMoney(String userId, double amount) {
        Wallet wallet = getOrCreateWallet(userId);

        if (amount > wallet.getBalance()) {
            throw new IllegalArgumentException("Insufficient Wallet Balance!");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
        return new WalletResponseDTO(userId, wallet.getBalance());
    }
}
