////package com.ecommerce.backend.entity;
////
////import jakarta.persistence.*;
////
////import lombok.*;
////
////@Data
////@Entity
////@Table(name = "categories")
////public class Category {
////
////    @Id
//////  @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long cid;
////
////    private String cname;
////    private String ctype;
////
////}
//
package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;
@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String cname;

    private String cphoto;

    @ManyToOne
    @JoinColumn(name = "sid")
    private SubCategory subCategory;

}


