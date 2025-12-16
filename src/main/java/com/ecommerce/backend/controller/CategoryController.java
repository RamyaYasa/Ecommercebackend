////package com.ecommerce.backend.controller;
////
////import com.ecommerce.backend.entity.Category;
////import com.ecommerce.backend.service.CategoryService;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/admin/categories")
////@CrossOrigin("*")
////public class CategoryController {
////    private final CategoryService categoryService;
////
////    public CategoryController(CategoryService categoryService) {
////        this.categoryService = categoryService;
////    }
////
////    // CREATE Category
////    @PostMapping("/add")
////    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
////        return ResponseEntity.ok(categoryService.addCategory(category));
////    }
////
////    // GET All Categories
////    @GetMapping("/all")
////    public ResponseEntity<List<Category>> getAllCategories() {
////        return ResponseEntity.ok(categoryService.getAllCategories());
////    }
////    @GetMapping("/{cname}")
////    public ResponseEntity<List<Category>> getCategoryByName(@PathVariable String cname) {
////        List<Category> category = categoryService.getCategoryByName(cname);
////
////        if (category == null) {
////            return ResponseEntity.notFound().build();
////        }
////
////        return ResponseEntity.ok(category);
////    }
////    @PutMapping("/update/{cid}")
////    public ResponseEntity<Category> updateCategory(@PathVariable Long cid,
////                                                   @RequestBody Category updatedCategory) {
////
////        Category category = categoryService.updateCategory(cid, updatedCategory);
////
////        if (category == null) {
////            return ResponseEntity.notFound().build();
////        }
////
////        return ResponseEntity.ok(category);
////    }
////
////    // DELETE Category
////    @DeleteMapping("/delete/{cid}")
////    public ResponseEntity<String> deleteCategory(@PathVariable Long cid) {
////
////        boolean deleted = categoryService.deleteCategory(cid);
////
////        if (!deleted) {
////            return ResponseEntity.notFound().build();
////        }
////
////        return ResponseEntity.ok("Category deleted successfully with id: " + cid);
////    }
//
//package com.ecommerce.backend.controller;
//import com.ecommerce.backend.dto.CategoryDTO;
//import com.ecommerce.backend.service.CategoryService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/categories")
//@CrossOrigin("*")
//public class CategoryController {
//
//    private final CategoryService categoryService;
//
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO dto) {
//        return ResponseEntity.ok(categoryService.addCategory(dto));
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
//        return ResponseEntity.ok(categoryService.getAllCategories());
//    }
//
//    @GetMapping("/category/{cname}")
//    public ResponseEntity<List<CategoryDTO>> getCategoryByName(@PathVariable String cname) {
//        List<CategoryDTO> category = categoryService.getCategoryByName(cname);
//
//        if (category.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(category);
//    }
//
//    @PutMapping("/update/{cid}")
//    public ResponseEntity<CategoryDTO> updateCategory(
//            @PathVariable Long cid,
//            @RequestBody CategoryDTO dto) {
//
//        CategoryDTO updated = categoryService.updateCategory(cid, dto);
//        return ResponseEntity.ok(updated);
//    }
//    @DeleteMapping("/delete/{cid}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long cid) {
//        categoryService.deleteCategory(cid);
//        return ResponseEntity.ok("Category deleted successfully!");
//    }
//}

package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CategoryDTO;
import com.ecommerce.backend.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /* ---------------- CREATE ---------------- */

    @PostMapping(value = "/addimage/add", consumes = "multipart/form-data")
    public ResponseEntity<CategoryDTO> addCategory(
            @RequestPart("category") String categoryJson,
            @RequestPart("image") MultipartFile imageFile
    ) throws IOException {

        CategoryDTO dto = new ObjectMapper()
                .readValue(categoryJson, CategoryDTO.class);

        return ResponseEntity.ok(categoryService.addCategory(dto, imageFile));
    }

    /* ---------------- READ ---------------- */

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{cid}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long cid) {
        return ResponseEntity.ok(categoryService.getCategoryById(cid));
    }
    @GetMapping("/category/{cname}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable String cname) {
        return ResponseEntity.ok(categoryService.getCategoryByCname(cname));
    }

    /* ---------------- UPDATE ---------------- */

    @PutMapping(value = "/update/{cid}", consumes = "multipart/form-data")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long cid,
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        CategoryDTO dto = new ObjectMapper()
                .readValue(categoryJson, CategoryDTO.class);

        return ResponseEntity.ok(
                categoryService.updateCategory(cid, dto, imageFile)
        );
    }
//    @GetMapping("/{cname}")
//    public ResponseEntity<CategoryDTO> getCategory(@PathVariable String cname) {
//        return ResponseEntity.ok(categoryService.getCategoryByCname(cname));
//    }

    /* ---------------- DELETE ---------------- */

    @DeleteMapping("/delete/{cid}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long cid) {
        categoryService.deleteCategory(cid);
        return ResponseEntity.ok("Category deleted successfully");
    }

    /* ---------------- IMAGE FETCH ---------------- */

    @GetMapping("/image/{cid}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable Long cid)
            throws IOException {

        byte[] image = categoryService.getCategoryImage(cid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
