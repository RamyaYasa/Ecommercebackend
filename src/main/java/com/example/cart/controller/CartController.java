package com.example.cart.controller;

import com.example.cart.client.AuthClient;
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
    private final AuthClient authClient;

    public CartController(
            CartService cartService,
            CouponService couponService,
            AuthClient authClient
    ) {
        this.cartService = cartService;
        this.couponService = couponService;
        this.authClient = authClient;
    }

    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CartItemRequestDTO request
    ) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }


    // Update item quantity
    @PutMapping("/item/{itemId}/quantity")
    public ResponseEntity<CartResponseDTO> updateQuantity(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.updateQuantity(userId, itemId, quantity));
    }

    // Delete item from cart
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<CartResponseDTO> deleteItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long itemId) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.deleteItem(userId, itemId));
    }

    // Clear entire cart
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponseDTO> clearCart( @RequestHeader("Authorization") String authorizationHeader) {
        String userId = authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    // Get full cart
    @GetMapping("/byId")
    public ResponseEntity<CartResponseDTO> getCart(@RequestHeader("Authorization") String authorizationHeader) {
        String userId=authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // User: Get all coupons from Cart API
    @GetMapping("/coupons/all")
    public ResponseEntity<List<CouponResponseDTO>> getAllCouponsForUser(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // Apply coupon
    @PostMapping("/apply-coupon/{couponCode}")
    public ResponseEntity<CartResponseDTO> applyCoupon(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String couponCode) {
        String userId=authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.applyCoupon(userId, couponCode));
    }

    // Remove coupon
    @DeleteMapping("remove-coupon")
    public ResponseEntity<CartResponseDTO> removeCoupon(@RequestHeader("Authorization") String authorizationHeader) {
        String userId=authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.removeCoupon(userId));
    }

    // Checkout
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestHeader("Authorization") String authorizationHeader) {
        String userId=authClient.extractClaims(authorizationHeader);
        return ResponseEntity.ok(cartService.checkout(userId));
    }
}
