package com.example.cart.repository;

import com.example.cart.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    void deleteByItemId(UUID itemId);
}
