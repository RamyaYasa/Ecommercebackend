package com.example.cart.service;

import com.example.cart.model.Wishlist;
import com.example.cart.model.WishlistItem;
import com.example.cart.repository.WishlistItemRepository;
import com.example.cart.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;

    // ------------------ GET OR CREATE ------------------

    public Wishlist getOrCreateWishlist(String userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist wishlist = Wishlist.builder()
                            .wishlistId(UUID.randomUUID())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .build();
                    return wishlistRepository.save(wishlist);
                });
    }

    // ------------------ ADD ITEM ------------------

    @Transactional
    public Wishlist addItem(String userId, WishlistItem item) {

        Wishlist wishlist = getOrCreateWishlist(userId);

        // ❌ prevent duplicate product in wishlist
        boolean exists = wishlist.getItems().stream()
                .anyMatch(i -> i.getPid().equals(item.getPid()));

        if (exists) {
            throw new IllegalArgumentException("Product already exists in wishlist");
        }

        item.setItemId(UUID.randomUUID());
        item.setWishlist(wishlist);

        wishlist.getItems().add(item);
        wishlistItemRepository.save(item);

        return wishlistRepository.save(wishlist);
    }

    // ------------------ REMOVE ITEM ------------------

    @Transactional
    public Wishlist removeItem(String userId, UUID itemId) {

        Wishlist wishlist = getOrCreateWishlist(userId);

        WishlistItem item = wishlist.getItems().stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in wishlist"));

        wishlist.getItems().remove(item);
        wishlistItemRepository.delete(item);

        return wishlistRepository.save(wishlist);
    }

    // ------------------ GET WISHLIST ------------------

    public Wishlist getWishlist(String userId) {
        return getOrCreateWishlist(userId);
    }

    // ------------------ CLEAR WISHLIST ------------------

    @Transactional
    public void clearWishlist(String userId) {

        Wishlist wishlist = getOrCreateWishlist(userId);

        wishlistItemRepository.deleteAll(wishlist.getItems());
        wishlist.getItems().clear();

        wishlistRepository.save(wishlist);
    }
}
