package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String pname;

    private String description;

    private double price;

    private int stockQuantity;

    private String imagePath;

    @Column(name = "actualprice", nullable = false)
    private double actualPrice = 0.0;

    private int discount;
    private double rating;
    private String qty;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;
}

