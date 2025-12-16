package com.example.cart.service;

import com.example.cart.model.Wishlist;
import com.example.cart.model.WishlistItem;
import com.example.cart.repository.WishlistRepository;
import com.example.cart.repository.WishlistItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;

    // Get or create wishlist
    public Wishlist getOrCreateWishlist(String userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> wishlistRepository.save(
                        Wishlist.builder()
                                .wishlistId(UUID.randomUUID())
                                .userId(userId)
                                .items(new ArrayList<>())
                                .build()
                ));
    }

    // Add item
    public Wishlist addItem(String userId, WishlistItem item) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        item.setItemId(UUID.randomUUID());
        item.setWishlist(wishlist);

        wishlistItemRepository.save(item);
        wishlist.getItems().add(item);

        return wishlistRepository.save(wishlist);
    }

    // Remove item
    @Transactional
    public Wishlist removeItem(String userId, UUID itemId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        wishlist.getItems().removeIf(i -> i.getItemId().equals(itemId));

        wishlistItemRepository.deleteByItemId(itemId);

        return wishlistRepository.save(wishlist);
    }

    // Get wishlist
    public Wishlist getWishlist(String userId) {
        return getOrCreateWishlist(userId);
    }

    // Clear wishlist
    public void clearWishlist(String userId) {
        Wishlist wishlist = getOrCreateWishlist(userId);

        wishlistItemRepository.deleteAll(wishlist.getItems());
        wishlist.getItems().clear();

        wishlistRepository.save(wishlist);
    }
}
