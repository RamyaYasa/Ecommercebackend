package com.example.cart.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "Wishlist")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

    @Id
    @Column(name = "wishlist_id")
    private UUID wishlistId;

    @Column(nullable = false)
    private String userId;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items = new ArrayList<>();
}
