//package com.ecommerce.backend.service;
//
//import com.ecommerce.backend.dto.SubCategoryDTO;
//import com.ecommerce.backend.entity.SubCategory;
//import com.ecommerce.backend.exception.ImageUploadException;
//import com.ecommerce.backend.exception.NotFoundException;
//import com.ecommerce.backend.repository.SubCategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.ecommerce.backend.util.ImageUrlBuilder;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Service
//public class SubCategoryService {
//
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//
//
//    @Autowired
//    private S3Service s3Service;
//
//    @Autowired
//    private ImageUrlBuilder imageUrlBuilder;
////
////    private final String UPLOAD_DIR =
////            "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
//
//    /* ---------------- DTO ↔ ENTITY ---------------- */
//
//    private SubCategoryDTO toDTO(SubCategory sub) {
//        return new SubCategoryDTO(
//                sub.getSid(),
//                sub.getSname(),
//                sub.getSphoto()
//        );
//    }
//
//    private SubCategory toEntity(SubCategoryDTO dto) {
//        SubCategory sub = new SubCategory();
//        sub.setSid(dto.getSid());
//        sub.setSname(dto.getSname());
//        sub.setSphoto(dto.getSphoto());
//        return sub;
//    }
//
//    /* ---------------- CREATE ---------------- */
//
////    public SubCategoryDTO addSubCategory(SubCategoryDTO dto, MultipartFile imageFile) {
////
////        if (imageFile == null || imageFile.isEmpty()) {
////            throw new ImageUploadException("SubCategory image is required");
////        }
////
////        String imagePath = saveImage(imageFile);
////        dto.setSphoto(imagePath);
////
////        SubCategory saved = subCategoryRepository.save(toEntity(dto));
////        return toDTO(saved);
////    }
//public SubCategoryDTO addSubCategory(
//        SubCategoryDTO dto,
//        MultipartFile imageFile) throws IOException {
//    if (imageFile == null || imageFile.isEmpty()) {
//        throw new ImageUploadException("SubCategory image is required");
//    }
////    SubCategory sub = new SubCategory();
//    SubCategory sub = toEntity(dto);
//    sub.setSname(dto.getSname());
//    SubCategory saved= subCategoryRepository.save(sub);
//
//
//    // 2️⃣ Upload image to S3
//    s3Service.uploadImage(imageFile,"subcategories",saved.getSid());
////    String imageKey = s3Service.uploadImage(imageFile);
//    String imageUrl= imageUrlBuilder.buildImageUrl("subcategories",saved.getSid());
//    saved.setSphoto(imageUrl);
//    subCategoryRepository.save(saved);
//    SubCategory finalSaved =
//            subCategoryRepository.save(saved);
//
//    return toDTO(finalSaved);
//}
//
//
//
//    /* ---------------- READ ---------------- */
//
//    public List<SubCategoryDTO> getAllSubCategories() {
//        List<SubCategoryDTO> list= subCategoryRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//        if(list.isEmpty()){
//            throw new NotFoundException("No subcategories found");
//        }
//        return list;
//    }
//
//    public SubCategoryDTO getSubCategoryById(Long sid) {
//        SubCategory sub = subCategoryRepository.findById(sid)
//                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//        return toDTO(sub);
//    }
//
//    public SubCategoryDTO getSubCategoryBySname(String sname) {
//        List<SubCategory> list = subCategoryRepository.findBySname(sname);
//        SubCategory sub = list.stream()
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException("Category not found"));
//        return toDTO(sub);
//    }
//
//    /* ---------------- UPDATE ---------------- */
//
////    public SubCategoryDTO updateSubCategory(
////            Long sid,
////            SubCategoryDTO dto,
////            MultipartFile imageFile) {
////
////        SubCategory sub = subCategoryRepository.findById(sid)
////                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
////
////        sub.setSname(dto.getSname());
////
////        if (imageFile != null && !imageFile.isEmpty()) {
////            String imagePath = saveImage(imageFile);
////            sub.setSphoto(imagePath);
////        }
////
////        return toDTO(subCategoryRepository.save(sub));
////    }
//
//    public SubCategoryDTO updateSubCategory(
//            Long sid,
//            SubCategoryDTO dto,
//            MultipartFile imageFile) throws IOException {
//
//        SubCategory sub = subCategoryRepository.findById(sid)
//                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//        dto.setSid(dto.getSid());
//        dto.setSname(dto.getSname());
//        dto.setSphoto(dto.getSphoto());
//        SubCategory sub=toEntity(dto);
//
//        if (imageFile != null && !imageFile.isEmpty()) {
//            if (sub.getSphoto()!=null){
//            s3Service.deleteImage(sub.getSphoto()); // delete old image
//            }
//            SubCategory saved1 = subCategoryRepository.save(sub);
//
//            // 2️⃣ Upload image to S3
//
//            s3Service.uploadImage(imageFile,"subcategories",saved1.getSid());
//            String imageUrl = imageUrlBuilder
//                    .buildImageUrl("subcategories",saved1.getSid());
////            String newKey = s3Service.uploadImage(imageFile);
//            sub.setSphoto(imageUrl);
//        }
//        SubCategory updated = subCategoryRepository.save(sub);
//        return toDTO(updated);
//    }
//
//
//    /* ---------------- DELETE ---------------- */
//
////    public void deleteSubCategory(Long sid) {
////        if (!subCategoryRepository.existsById(sid)) {
////            throw new NotFoundException("SubCategory not found");
////        }
////        subCategoryRepository.deleteById(sid);
////    }
//public void deleteSubCategory(Long sid) {
//
//    SubCategory sub = subCategoryRepository.findById(sid)
//            .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//
//    s3Service.deleteImage(sub.getSphoto());
//    subCategoryRepository.delete(sub);
//}
//
//
//    /* ---------------- IMAGE FETCH ---------------- */
//
////    public byte[] getSubCategoryImage(Long sid) throws IOException {
////        SubCategory sub = subCategoryRepository.findById(sid)
////                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
////
////        File file = new File(sub.getSphoto());
////        if (!file.exists()) {
////            throw new NotFoundException("Image not found");
////        }
////
////        return Files.readAllBytes(file.toPath());
////    }
//public byte[] getSubCategoryImage(Long sid) {
//
//    SubCategory sub = subCategoryRepository.findById(sid)
//            .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//
//    return s3Service.getImage(sub.getSphoto());
//}
//
//    /* ---------------- IMAGE SAVE ---------------- */
//
////    private String saveImage(MultipartFile file) {
////        try {
////            File dir = new File(UPLOAD_DIR);
////            if (!dir.exists()) dir.mkdirs();
////
////            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
////            String fullPath = UPLOAD_DIR + fileName;
////
////            file.transferTo(new File(fullPath));
////            return fullPath;
////
////        } catch (IOException e) {
////            throw new ImageUploadException("Failed to upload image");
////        }
////    }
//}
