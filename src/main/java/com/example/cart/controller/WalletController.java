package com.example.cart.controller;

import com.example.cart.dto.WalletResponseDTO;
import com.example.cart.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cart.client.AuthClient;


@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final AuthClient authClient;


    public WalletController(
            WalletService walletService,
            AuthClient authClient
    )
    {
        this.walletService = walletService;
        this.authClient = authClient;
    }

    @GetMapping("/byId")
    public ResponseEntity<WalletResponseDTO>
    getWallet(@RequestHeader("Authorization") String authorizationHeader)
    {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(walletService.getWallet(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<WalletResponseDTO> addMoney(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam double amount)
    {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(walletService.addMoney(userId, amount));
    }

    @PostMapping("/pay")
    public ResponseEntity<WalletResponseDTO> pay(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam double amount)
    {
        String userId = authClient.extractClaims(authorizationHeader);

        return ResponseEntity.ok(walletService.deductMoney(userId, amount));
    }
}