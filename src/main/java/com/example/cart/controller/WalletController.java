package com.example.cart.controller;

import com.example.cart.dto.WalletResponseDTO;
import com.example.cart.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponseDTO> getWallet(@PathVariable String userId) {
        return ResponseEntity.ok(walletService.getWallet(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<WalletResponseDTO> addMoney(
            @PathVariable String userId,
            @RequestParam double amount) {
        return ResponseEntity.ok(walletService.addMoney(userId, amount));
    }

    @PostMapping("/{userId}/pay")
    public ResponseEntity<WalletResponseDTO> pay(
            @PathVariable String userId,
            @RequestParam double amount) {
        return ResponseEntity.ok(walletService.deductMoney(userId, amount));
    }
}

