package com.example.cart.service;

import com.example.cart.client.ProductClient;
import com.example.cart.dto.ProductDTO;
import com.example.cart.dto.WishlistItemRequestDTO;
import com.example.cart.model.CartItem;
import com.example.cart.model.Wishlist;
import com.example.cart.model.WishlistItem;
import com.example.cart.repository.WishlistItemRepository;
import com.example.cart.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductClient productClient;

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
    public Wishlist addItem(String userId, WishlistItemRequestDTO request) {

        ProductDTO product = productClient.getProductById(request.getPid());

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product not found with ID: " + request.getPid());
        }

        Wishlist wishlist = getOrCreateWishlist(userId);

        Optional<WishlistItem> existingItemOpt = wishlist.getItems().stream()
                .filter(i -> i.getPid().equals(request.getPid()))
                .findFirst();

        // ✅ If item already exists → just return wishlist
        if (existingItemOpt.isPresent()) {
            return wishlist;
        }

        // ✅ Create new item only if not exists
        WishlistItem newItem = WishlistItem.builder()
                .pid(product.getPid())
                .pname(product.getPname())
                .description(product.getDescription())
                .price(product.getPrice())
                .actualPrice(
                        product.getActualPrice() != 0.0
                                ? product.getActualPrice()
                                : product.getPrice())
                .discount(product.getDiscount())
                .stockQuantity(product.getStockQuantity())
                .rating(product.getRating())
                .imagePath(product.getImagePath())
                .wishlist(wishlist)
                .build();

        wishlist.getItems().add(newItem);

        wishlistItemRepository.save(newItem);
        return wishlistRepository.save(wishlist);
    }



//        if (existing.isPresent()) {
//            Wishlist item = existing.get();
//            //int newQty = item.getQuantity() + request.getQuantity();
//
//            // Check stock using product from Product service
//            if (newQty > product.getStockQuantity()) {
//                throw new IllegalArgumentException(
//                        "Requested quantity exceeds available stock. Available: " +
//                                product.getStockQuantity()
//                );
//            }


        // ❌ prevent duplicate product in wishlist
//        boolean exists = product.getItems().stream()
//                .anyMatch(i -> i.getPid().equals(item.getPid()));
//
//        if (exists) {
//            throw new IllegalArgumentException("Product already exists in wishlist");
//        }
//
//        item.setItemId(UUID.randomUUID());
//        item.setWishlist(wishlist);
//
//        wishlist.getItems().add(item);
//        wishlistItemRepository.save(item);
//
//        return wishlistRepository.save(wishlist);


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
