package com.example.cart.controller;

import com.example.cart.model.Wishlist;
import com.example.cart.model.WishlistItem;
import com.example.cart.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<Wishlist> addItem(
            @PathVariable String userId,
            @RequestBody WishlistItem item
    ) {
        return ResponseEntity.ok(wishlistService.addItem(userId, item));
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<Wishlist> removeItem(
            @PathVariable String userId,
            @PathVariable UUID itemId
    ) {
        return ResponseEntity.ok(wishlistService.removeItem(userId, itemId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Wishlist> getWishlist(@PathVariable String userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearWishlist(@PathVariable String userId) {
        wishlistService.clearWishlist(userId);
        return ResponseEntity.ok("Wishlist cleared successfully.");
    }
}
