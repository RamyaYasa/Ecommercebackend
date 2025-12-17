package com.example.cart.client;

import com.example.cart.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${product.service.url}"
)
public interface ProductClient {


    @GetMapping("/api/admin/products/{pid}")
    ProductDTO getProductById(@PathVariable("pid") Long pid);


//    @GetMapping("/api/admin/products/batch")
//    List<ProductDTO> getProductsByIds(@RequestParam("ids") List<Long> productIds);


    @GetMapping("/api/admin/products/all")
    List<ProductDTO> getAllProducts();


    @GetMapping("/api/admin/products/category/{cid}")
    List<ProductDTO> getProductsByCategory(@PathVariable("cid") Long categoryId);
}