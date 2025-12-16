//package com.ecommerce.backend.service;
//
//import com.ecommerce.backend.dto.SubCategoryDTO;
//import com.ecommerce.backend.entity.SubCategory;
//import com.ecommerce.backend.exception.NotFoundException;
//import com.ecommerce.backend.repository.SubCategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class SubCategoryService {
//
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
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
//    public SubCategoryDTO create(SubCategoryDTO dto) {
//        SubCategory saved = subCategoryRepository.save(toEntity(dto));
//        return toDTO(saved);
//    }
//
//    public List<SubCategoryDTO> getAll() {
//        return subCategoryRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public SubCategoryDTO getById(Long sid) {
//        SubCategory sub = subCategoryRepository.findById(sid)
//                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//        return toDTO(sub);
//    }
//
//    public SubCategoryDTO update(Long sid, SubCategoryDTO dto) {
//        SubCategory sub = subCategoryRepository.findById(sid)
//                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
//
//        sub.setSname(dto.getSname());
//        sub.setSphoto(dto.getSphoto());
//
//        return toDTO(subCategoryRepository.save(sub));
//    }
//
//    public void delete(Long sid) {
//        if (!subCategoryRepository.existsById(sid)) {
//            throw new NotFoundException("SubCategory not found");
//        }
//        subCategoryRepository.deleteById(sid);
//    }
//}



package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.SubCategoryDTO;
import com.ecommerce.backend.entity.SubCategory;
import com.ecommerce.backend.exception.ImageUploadException;
import com.ecommerce.backend.exception.NotFoundException;
import com.ecommerce.backend.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private final String UPLOAD_DIR =
            "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";

    /* ---------------- DTO ↔ ENTITY ---------------- */

    private SubCategoryDTO toDTO(SubCategory sub) {
        return new SubCategoryDTO(
                sub.getSid(),
                sub.getSname(),
                sub.getSphoto()
        );
    }

    private SubCategory toEntity(SubCategoryDTO dto) {
        SubCategory sub = new SubCategory();
        sub.setSid(dto.getSid());
        sub.setSname(dto.getSname());
        sub.setSphoto(dto.getSphoto());
        return sub;
    }

    /* ---------------- CREATE ---------------- */

    public SubCategoryDTO addSubCategory(SubCategoryDTO dto, MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new ImageUploadException("SubCategory image is required");
        }

        String imagePath = saveImage(imageFile);
        dto.setSphoto(imagePath);

        SubCategory saved = subCategoryRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    /* ---------------- READ ---------------- */

    public List<SubCategoryDTO> getAllSubCategories() {
        return subCategoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SubCategoryDTO getSubCategoryById(Long sid) {
        SubCategory sub = subCategoryRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("SubCategory not found"));
        return toDTO(sub);
    }

    /* ---------------- UPDATE ---------------- */

    public SubCategoryDTO updateSubCategory(
            Long sid,
            SubCategoryDTO dto,
            MultipartFile imageFile) {

        SubCategory sub = subCategoryRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("SubCategory not found"));

        sub.setSname(dto.getSname());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            sub.setSphoto(imagePath);
        }

        return toDTO(subCategoryRepository.save(sub));
    }

    /* ---------------- DELETE ---------------- */

    public void deleteSubCategory(Long sid) {
        if (!subCategoryRepository.existsById(sid)) {
            throw new NotFoundException("SubCategory not found");
        }
        subCategoryRepository.deleteById(sid);
    }

    /* ---------------- IMAGE FETCH ---------------- */

    public byte[] getSubCategoryImage(Long sid) throws IOException {
        SubCategory sub = subCategoryRepository.findById(sid)
                .orElseThrow(() -> new NotFoundException("SubCategory not found"));

        File file = new File(sub.getSphoto());
        if (!file.exists()) {
            throw new NotFoundException("Image not found");
        }

        return Files.readAllBytes(file.toPath());
    }

    /* ---------------- IMAGE SAVE ---------------- */

    private String saveImage(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String fullPath = UPLOAD_DIR + fileName;

            file.transferTo(new File(fullPath));
            return fullPath;

        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image");
        }
    }
}
