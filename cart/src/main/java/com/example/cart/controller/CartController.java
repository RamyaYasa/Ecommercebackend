package com.example.cart.controller;

import com.example.cart.dto.CartItemRequestDTO;
import com.example.cart.dto.CartResponseDTO;
import com.example.cart.dto.CouponResponseDTO;
import com.example.cart.service.CartService;
import com.example.cart.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CouponService couponService;

    public CartController(CartService cartService, CouponService couponService) {
        this.cartService = cartService;
        this.couponService = couponService;
    }

    // Add item to cart
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @PathVariable String userId,
            @Valid @RequestBody CartItemRequestDTO request) {

        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    // Update item quantity
    @PutMapping("/{userId}/item/{itemId}/quantity")
    public ResponseEntity<CartResponseDTO> updateQuantity(
            @PathVariable String userId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(cartService.updateQuantity(userId, itemId, quantity));
    }

    // Delete item from cart
    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<CartResponseDTO> deleteItem(
            @PathVariable String userId,
            @PathVariable Long itemId) {

        return ResponseEntity.ok(cartService.deleteItem(userId, itemId));
    }

    // Clear entire cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    // Get full cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // User: Get all coupons from Cart API
    @GetMapping("/{userId}/coupons/all")
    public ResponseEntity<List<CouponResponseDTO>> getAllCouponsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // Apply coupon
    @PostMapping("/{userId}/apply-coupon/{couponCode}")
    public ResponseEntity<CartResponseDTO> applyCoupon(
            @PathVariable String userId,
            @PathVariable String couponCode) {

        return ResponseEntity.ok(cartService.applyCoupon(userId, couponCode));
    }

    // Remove coupon
    @DeleteMapping("/{userId}/remove-coupon")
    public ResponseEntity<CartResponseDTO> removeCoupon(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.removeCoupon(userId));
    }

    // Checkout
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.checkout(userId));
    }

}
