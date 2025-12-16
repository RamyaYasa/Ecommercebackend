package com.example.cart.service;

import com.example.cart.dto.CartItemRequestDTO;
import com.example.cart.dto.CartResponseDTO;

public interface CartService {

    CartResponseDTO addToCart(String userId, CartItemRequestDTO request);

    CartResponseDTO updateQuantity(String userId, Long itemId, int newQuantity);

    CartResponseDTO deleteItem(String userId, Long itemId);

    CartResponseDTO clearCart(String userId);

    CartResponseDTO getCart(String userId);

    CartResponseDTO applyCoupon(String userId, String couponCode);

    CartResponseDTO removeCoupon(String userId);

    String checkout(String userId);

}
