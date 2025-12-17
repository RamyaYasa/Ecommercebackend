//package com.ecommerce.backend.controller;
//
//import com.ecommerce.backend.dto.SubCategoryDTO;
//import com.ecommerce.backend.service.SubCategoryService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/subcategories")
//@CrossOrigin("*")
//public class SubCategoryController {
//
//    @Autowired
//    private SubCategoryService subCategoryService;
//
//    @PostMapping(value = "addimage/add", consumes = "multipart/form-data")
//    public ResponseEntity<SubCategoryDTO> addSubCategory(
//            @RequestPart("subcategory") String subCategoryJson,
//            @RequestPart("image") MultipartFile imageFile) throws IOException {
//
//        SubCategoryDTO dto =
//                new ObjectMapper().readValue(subCategoryJson, SubCategoryDTO.class);
//
//        return ResponseEntity.ok(
//                subCategoryService.addSubCategory(dto, imageFile));
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<SubCategoryDTO>> getAll() {
//        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
//    }
//
//    @GetMapping("/{sid}")
//    public ResponseEntity<SubCategoryDTO> getById(@PathVariable Long sid) {
//        return ResponseEntity.ok(subCategoryService.getSubCategoryById(sid));
//    }
//    @GetMapping("subcategory/{sname}")
//    public ResponseEntity<SubCategoryDTO> getBySname(@PathVariable String sname) {
//        return ResponseEntity.ok(subCategoryService.getSubCategoryBySname(sname));
//    }
//
//    @PutMapping(value = "/update/{sid}", consumes = "multipart/form-data")
//    public ResponseEntity<SubCategoryDTO> update(
//            @PathVariable Long sid,
//            @RequestPart("subcategory") String subCategoryJson,
//            @RequestPart(value = "image", required = false) MultipartFile imageFile
//    ) throws IOException {
//
//        SubCategoryDTO dto =
//                new ObjectMapper().readValue(subCategoryJson, SubCategoryDTO.class);
//
//        return ResponseEntity.ok(
//                subCategoryService.updateSubCategory(sid, dto, imageFile));
//    }
//
//    @DeleteMapping("/delete/{sid}")
//    public ResponseEntity<String> delete(@PathVariable Long sid) {
//        subCategoryService.deleteSubCategory(sid);
//        return ResponseEntity.ok("SubCategory deleted successfully");
//    }
//
//    @GetMapping("/image/{sid}")
//    public ResponseEntity<byte[]> getImage(@PathVariable Long sid) throws IOException {
//
//        byte[] image = subCategoryService.getSubCategoryImage(sid);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//
//        return new ResponseEntity<>(image, headers, HttpStatus.OK);
//    }
//}
