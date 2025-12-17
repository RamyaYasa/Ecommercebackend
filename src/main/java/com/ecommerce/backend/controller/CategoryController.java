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
