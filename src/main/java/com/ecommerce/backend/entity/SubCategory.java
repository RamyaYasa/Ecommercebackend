package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subcategory")
public class SubCategory {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    private String sname;

    private String sphoto;
}
