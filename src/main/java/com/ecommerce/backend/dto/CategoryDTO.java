//package com.ecommerce.backend.dto;
//
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class CategoryDTO {
//
//    private Long cid;
//    private String cname;
//    private String ctype;
//}



package com.ecommerce.backend.dto;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long cid;
    private String cname;
    private String cphoto;
    private SubCategoryDTO subCategory;


    // getters & setters
}
