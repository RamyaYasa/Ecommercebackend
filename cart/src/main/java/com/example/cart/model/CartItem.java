package com.example.cart.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pid; // product id
    private String pname;

    @Column(length = 1000)
    private String description;

    private double price;          // selling price
    private double actualPrice;    // MRP
    private int discount;          // %
    private int stockQuantity;     // available stock
    private int quantity;          // quantity user added

    private double totalPrice;     // price * quantity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Calculate total price
    public void recalcTotal() {
        this.totalPrice = this.price * this.quantity;
    }
}
