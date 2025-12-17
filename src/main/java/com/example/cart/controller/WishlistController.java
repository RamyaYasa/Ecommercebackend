package com.example.cart.controller;
import com.example.cart.dto.WishlistItemRequestDTO;
import com.example.cart.model.Wishlist;
import com.example.cart.model.WishlistItem;
import com.example.cart.service.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cart.client.AuthClient;


import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final AuthClient authClient;


    @PostMapping("/add")
    public ResponseEntity<Wishlist> addItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody WishlistItemRequestDTO request
            ) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(wishlistService.addItem(userId, request));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Wishlist> removeItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable UUID itemId
    ) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(wishlistService.removeItem(userId, itemId));
    }

    @GetMapping("/byId")
    public ResponseEntity<Wishlist>
    getWishlist(@RequestHeader("Authorization") String authorizationHeader) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String>
    clearWishlist(@RequestHeader("Authorization")String authorizationHeader)
    {
        String userId =authClient.extractClaims(authorizationHeader);
        wishlistService.clearWishlist(userId);
        return ResponseEntity.ok("Wishlist cleared successfully.");
    }
}