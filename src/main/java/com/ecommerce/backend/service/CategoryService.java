//package com.ecommerce.backend.service;
//import com.ecommerce.backend.dto.CategoryDTO;
//import com.ecommerce.backend.entity.Category;
//
//import com.ecommerce.backend.exception.NotFoundException;
//import com.ecommerce.backend.exception.AlreadyExistsException;
//import com.ecommerce.backend.repository.CategoryRepository;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class CategoryService {
//
//    private final CategoryRepository categoryRepository;
//
//    public CategoryService(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    private CategoryDTO toDTO(Category category) {
//        return new CategoryDTO(
//                category.getCid(),
//                category.getCname(),
//                category.getCtype()
//        );
//    }
//
//    private Category toEntity(CategoryDTO dto) {
//        Category category = new Category();
//        category.setCid(dto.getCid());
//        category.setCname(dto.getCname());
//        category.setCtype(dto.getCtype());
//        return category;
//    }
//
//    public CategoryDTO addCategory(CategoryDTO dto) {
//        Category existingCategory=categoryRepository.findByCid(dto.getCid());
//        if (existingCategory!=null){
//            throw new AlreadyExistsException("Category id with "+dto.getCid()+" already exists");
//        }
//        else{
//        Category saved = categoryRepository.save(toEntity(dto));
//        return toDTO(saved);
//        }
//    }
//
//    public List<CategoryDTO> getAllCategories() {
//        return categoryRepository.findAll()
//                .stream()
//                .map(this::toDTO)
//                .toList();
//    }
//
////    public List<CategoryDTO> getCategoryByName(String cname) {
////        return categoryRepository.findByCname(cname).orElseThrow(
////                        () -> new CategoryNotFoundException("Category Not Found"))
////                .stream()
////                .map(this::toDTO)
////                .toList();
////    }
//
//    public List<CategoryDTO> getCategoryByName(String cname) {
//        List<Category> categories = categoryRepository.findByCname(cname);
//
//        if (categories.isEmpty()) {
//            throw new NotFoundException(cname+" Category Not Found");
//        }
//
//        return categories.stream()
//                .map(this::toDTO)
//                .toList();
//    }
//
//    public CategoryDTO updateCategory(Long cid, CategoryDTO dto) {
//        Category category = categoryRepository.findById(cid)
//                .orElseThrow(() -> new NotFoundException("Category id with "+cid+" doesn't exists to update"));
//
//        category.setCname(dto.getCname());
//        category.setCtype(dto.getCtype());
//
//        return toDTO(categoryRepository.save(category));
//    }
//
//
//    public void deleteCategory(Long cid) {
//        if (!categoryRepository.existsById(cid)) {
//            throw new NotFoundException("Category id with "+cid+" doesn't exists to delete");
//        }
//        categoryRepository.deleteById(cid);
//    }
//}




package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDTO;
import com.ecommerce.backend.dto.SubCategoryDTO;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.SubCategory;
import com.ecommerce.backend.exception.ImageUploadException;
import com.ecommerce.backend.exception.NotFoundException;
import com.ecommerce.backend.repository.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private final String UPLOAD_DIR =
            "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";

    /* ---------------- DTO ↔ ENTITY ---------------- */

    private CategoryDTO toDTO(Category category) {

        SubCategoryDTO subDTO = null;
        if (category.getSubCategory() != null) {
            subDTO = new SubCategoryDTO(
                    category.getSubCategory().getSid(),
                    category.getSubCategory().getSname(),
                    category.getSubCategory().getSphoto()
            );
        }

        return new CategoryDTO(
                category.getCid(),
                category.getCname(),
                category.getCphoto(),
                subDTO
        );
    }

    private Category toEntity(CategoryDTO dto) {

        Category category = new Category();
        category.setCid(dto.getCid());
        category.setCname(dto.getCname());
        category.setCphoto(dto.getCphoto());

        if (dto.getSubCategory() != null) {
            SubCategory sub = subCategoryRepository.findById(dto.getSubCategory().getSid())
                    .orElseThrow(() -> new NotFoundException("SubCategory not found"));
            category.setSubCategory(sub);
        }

        return category;
    }

    /* ---------------- CREATE ---------------- */

    public CategoryDTO addCategory(CategoryDTO dto, MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new ImageUploadException("Category image is required");
        }

        String imagePath = saveImage(imageFile);
        dto.setCphoto(imagePath);

        Category saved = categoryRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    /* ---------------- READ ---------------- */

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long cid) {
        Category category = categoryRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return toDTO(category);
    }
    public CategoryDTO getCategoryByCname(String cname) {
//        Category category = categoryRepository.findByCname(cname);
//        if (category == null) {
//            throw new NotFoundException("Category not found");
//        }
        List<Category> list = categoryRepository.findByCname(cname);
        Category category = list.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Category not found"));

        return toDTO( category);
    }

//    public CategoryDTO getnameCategory(String cname) {
//        Category category = categoryRepository.findByCname(cname)
//                .orElseThrow(() -> new NotFoundException("Category not found"));
//        return toDTO(category);
//    }
    /* ---------------- UPDATE ---------------- */

    public CategoryDTO updateCategory(
            Long cid,
            CategoryDTO dto,
            MultipartFile imageFile) {

        Category category = categoryRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        category.setCname(dto.getCname());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            category.setCphoto(imagePath);
        }

        if (dto.getSubCategory() != null) {
            SubCategory sub = subCategoryRepository.findById(dto.getSubCategory().getSid())
                    .orElseThrow(() -> new NotFoundException("SubCategory not found"));
            category.setSubCategory(sub);
        }

        return toDTO(categoryRepository.save(category));
    }

    /* ---------------- DELETE ---------------- */

    public void deleteCategory(Long cid) {
        if (!categoryRepository.existsById(cid)) {
            throw new NotFoundException("Category not found");
        }
        categoryRepository.deleteById(cid);
    }

    /* ---------------- IMAGE FETCH ---------------- */

    public byte[] getCategoryImage(Long cid) throws IOException {

        Category category = categoryRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        File file = new File(category.getCphoto());
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
