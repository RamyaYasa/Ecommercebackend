package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{pid}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long pid) {
        return ResponseEntity.ok(productService.getProduct(pid));
    }

    @GetMapping("/category/{cid}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long cid) {
        return ResponseEntity.ok(productService.getProductsByCategory(cid));
    }
//    @PutMapping("/update/{pid}")
//    public ResponseEntity<ProductDTO> updateProduct(
//            @PathVariable Long pid,
//            @RequestBody ProductDTO dto) {
//        return ResponseEntity.ok(productService.updateProduct(pid, dto));
//    }
    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long pid) {
        productService.deleteProduct(pid);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    @PostMapping(value = "/addimage", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> addProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart(value="image",required=false) MultipartFile imageFile
    ) throws IOException {

        ProductDTO dto = new ObjectMapper().readValue(productJson, ProductDTO.class);
        return ResponseEntity.ok(productService.addProductWithImage(dto, imageFile));
    }
    @PutMapping(value = "/update/{pid}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> updateProductWithImage(
            @PathVariable Long pid,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {
        ProductDTO dto = new ObjectMapper().readValue(productJson, ProductDTO.class);
        ProductDTO updatedPro = productService.updateProductWithImage(pid, dto, imageFile);
        return ResponseEntity.ok(updatedPro);
    }
    @GetMapping("/image/{pid}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long pid) throws IOException {

        byte[] imageData = productService.getProductImage(pid);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, determineImageType(imageData));

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    private String determineImageType(byte[] data) {
        if (data.length > 3 &&
                data[0] == (byte) 0xFF &&
                data[1] == (byte) 0xD8) {
            return "image/jpeg";
        }
        if (data.length > 3 &&
                data[0] == (byte) 0x89 &&
                data[1] == (byte) 0x50) {
            return "image/png";
        }
        return "application/octet-stream";
    }

}
