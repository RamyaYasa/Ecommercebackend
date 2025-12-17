package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDTO;
import com.ecommerce.backend.dto.ProductDTO;
//import com.ecommerce.backend.dto.SubCategoryDTO;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.exception.*;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.backend.util.ImageUrlBuilder;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageUrlBuilder imageUrlBuilder;

    /* ===================== DTO → ENTITY ===================== */
    private Product toEntity(ProductDTO dto) {
        Product p = new Product();

        p.setPid(dto.getPid());
        p.setPname(dto.getPname());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStockQuantity(dto.getStockQuantity());
        p.setImagePath(dto.getImagePath());
        p.setActualPrice(dto.getActualPrice());
        p.setRating(dto.getRating());
        p.setQuantity(dto.getQuantity());
        p.setDiscount(dto.getDiscount());

        if (dto.getCategory() != null) {
            Category c = categoryRepository.findById(dto.getCategory().getCid())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            p.setCategory(c);
        }

        return p;
    }

    /* ===================== ENTITY → DTO ===================== */
    private ProductDTO toDTO(Product product) {

        CategoryDTO categoryDTO = null;

        if (product.getCategory() != null) {

//            SubCategoryDTO subDTO = null;
//            if (product.getCategory().getSubCategory() != null) {
//                subDTO = new SubCategoryDTO(
//                        product.getCategory().getSubCategory().getSid(),
//                        product.getCategory().getSubCategory().getSname(),
//                        product.getCategory().getSubCategory().getSphoto()
//                );
//            }

            categoryDTO = new CategoryDTO(
                    product.getCategory().getCid(),
                    product.getCategory().getCname(),
                    product.getCategory().getCphoto()
//                    subDTO
            );
        }

        return new ProductDTO(
                product.getPid(),
                product.getPname(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImagePath(),
                product.getActualPrice(),
                product.getDiscount(),
                product.getRating(),
                product.getQuantity(),
                categoryDTO
        );
    }

    /* ===================== GET ===================== */
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> list = productRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("There are no products exists");
        }
        return list;
    }

    public List<ProductDTO> getProductsByCategory(Long cid) {
        List<ProductDTO> list = productRepository.findByCategoryCid(cid)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("Category id " + cid + " doesn't exist in products");
        }
        return list;
    }

    public ProductDTO getProduct(Long pid) {
        return productRepository.findById(pid)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Product id " + pid + " doesn't exist"));
    }

    /* ===================== DELETE ===================== */
//    public void deleteProduct(Long pid) {
//        if (!productRepository.existsById(pid)) {
//            throw new NotFoundException("Product id " + pid + " doesn't exist");
//        }
//        productRepository.deleteById(pid);
//    }
    public void deleteProduct(Long pid) {

        Product product = productRepository.findById(pid)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));

        s3Service.deleteImage(product.getImagePath());
        productRepository.delete(product);
    }

    /* ===================== ADD WITH IMAGE ===================== */
//    public ProductDTO addProductWithImage(ProductDTO dto, MultipartFile file) {
//
//        if (file == null || file.isEmpty()) {
//            throw new InvalidRequestException("Image file is missing");
//        }
//
//        String uploadDir = "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
//        File folder = new File(uploadDir);
//
//        if (!folder.exists() && !folder.mkdirs()) {
//            throw new ImageUploadException("Failed to create image directory");
//        }
//
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        String filePath = uploadDir + fileName;
//
//        try {
//            file.transferTo(new File(filePath));
//        } catch (IOException e) {
//            throw new ImageUploadException("Image upload failed");
//        }
//
//        dto.setImagePath(filePath);
//        dto.setActualPrice(dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0));
//
//        if (productRepository.findByPid(dto.getPid()) != null) {
//            throw new AlreadyExistsException("Product id already exists");
//        }
//
//        Product saved = productRepository.save(toEntity(dto));
//        return toDTO(saved);
//    }
//    public ProductDTO addProductWithImage(
//            ProductDTO dto,
//            MultipartFile imageFile) throws IOException {
//        Product product = new Product();
//
//        if (imageFile == null || imageFile.isEmpty()) {
//            throw new InvalidRequestException("Product image is required");
//        }
//
//        if (productRepository.findByPid(dto.getPid()) != null) {
//            throw new AlreadyExistsException("Product id already exists");
//        }
////        Product saved1 = productRepository.save(product);
////        String imageKey = s3Service.uploadImage(imageFile);
//
//        Product saved1 = productRepository.save(product);
//        String newImageKey = s3Service.uploadImage(imageFile,"products",saved1.getPid());
//        dto.setImagePath(newImageKey);
//
//        double actualPrice =
//                dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0);
//        dto.setActualPrice(actualPrice);
//
//        Product saved = productRepository.save(toEntity(dto));
//        return toDTO(saved);
//    }
public ProductDTO addProductWithImage(
        ProductDTO dto,
        MultipartFile imageFile) throws IOException {

    if (imageFile == null || imageFile.isEmpty()) {
        throw new ImageUploadException("Product image is required");
    }

    /* 1️⃣ CALCULATE ACTUAL PRICE */
    double actualPrice =
            dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0);
    dto.setActualPrice(actualPrice);

    /* 2️⃣ CONVERT DTO → ENTITY */
    Product product = toEntity(dto);

    /* 3️⃣ SAVE FIRST TO GENERATE PID */
    Product saved = productRepository.save(product);

    /* 4️⃣ UPLOAD IMAGE TO S3 */
    s3Service.uploadImage(
            imageFile,
            "products",
            saved.getPid()
    );

    /* 5️⃣ BUILD IMAGE API URL */
    String imageUrl =
            imageUrlBuilder.buildImageUrl("products", saved.getPid());

    /* 6️⃣ STORE IMAGE URL IN ENTITY */
    saved.setImagePath(imageUrl);

    /* 7️⃣ SAVE AGAIN */
    Product finalSaved = productRepository.save(saved);

    return toDTO(finalSaved);
}

//    public ProductDTO updateProductWithImage(Long pid, ProductDTO dto, MultipartFile imageFile) {
//
//        try {
//            Product product = productRepository.findById(pid)
//                    .orElseThrow(() -> new NotFoundException("Product id " + pid + " does not exist."));
//
//            product.setPname(dto.getPname());
//            product.setDescription(dto.getDescription());
//            product.setPrice(dto.getPrice());
//            product.setRating(dto.getRating());
//            product.setQuantity(dto.getQuantity());
//            product.setStockQuantity(dto.getStockQuantity());
//            product.setDiscount(dto.getDiscount());
//
//            double discountAmount = (dto.getPrice() * dto.getDiscount()) / 100.0;
//            product.setActualPrice(dto.getPrice() - discountAmount);
//
//            if (imageFile != null && !imageFile.isEmpty()) {
//                String uploadDir = "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
//                File folder = new File(uploadDir);
//
//                if (!folder.exists() && !folder.mkdirs()) {
//                    throw new ImageUploadException("Failed to create upload folder.");
//                }
//
//                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//                String filePath = uploadDir + fileName;
//
//                try {
//                    imageFile.transferTo(new File(filePath));
//                } catch (IOException e) {
//                    throw new ImageUploadException("Image upload failed: " + e.getMessage());
//                }
//
//                product.setImagePath(filePath);
//            }
//
//            Category category = categoryRepository.findById(dto.getCategory().getCid())
//                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategory().getCid()));
//
//            product.setCategory(category);
//
//            Product saved = productRepository.save(product);
//            return toDTO(saved);
//
//        } catch (NotFoundException | ImageUploadException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            throw new InvalidRequestException("Unexpected error while updating product: " + ex.getMessage());
//        }
//    }
//public ProductDTO updateProductWithImage(
//        Long pid,
//        ProductDTO dto,
//        MultipartFile imageFile) throws IOException {
//
//    Product product = productRepository.findById(pid)
//            .orElseThrow(() ->
//                    new NotFoundException("Product not found"));
//
//    product.setPname(dto.getPname());
//    product.setDescription(dto.getDescription());
//    product.setPrice(dto.getPrice());
//    product.setRating(dto.getRating());
//    product.setQuantity(dto.getQuantity());
//    product.setStockQuantity(dto.getStockQuantity());
//    product.setDiscount(dto.getDiscount());
//
//    double actualPrice =
//            dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0);
//    product.setActualPrice(actualPrice);
//
//    if (imageFile != null && !imageFile.isEmpty()) {
//        s3Service.deleteImage(product.getImagePath());
//        Product saved1 = productRepository.save(product);
//        String newImageKey = s3Service.uploadImage(imageFile,"products",saved1.getPid());
////        String newImageKey = s3Service.uploadImage(imageFile);
//        product.setImagePath(newImageKey);
//    }
//
//    if (dto.getCategory() != null) {
//        Category category = categoryRepository.findById(dto.getCategory().getCid())
//                .orElseThrow(() ->
//                        new NotFoundException("Category not found"));
//        product.setCategory(category);
//    }
//
//    Product saved = productRepository.save(product);
//    return toDTO(saved);
//}
public ProductDTO updateProductWithImage(
        Long pid,
        ProductDTO dto,
        MultipartFile imageFile) throws IOException {

    /* 1️⃣ FETCH EXISTING PRODUCT */
    Product existing = productRepository.findById(pid)
            .orElseThrow(() ->
                    new NotFoundException("Product not found"));

    /* 2️⃣ PRESERVE PID & IMAGE */
    dto.setPid(pid);
    dto.setImagePath(existing.getImagePath());

    /* 3️⃣ RECALCULATE PRICE */
    double actualPrice =
            dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0);
    dto.setActualPrice(actualPrice);

    /* 4️⃣ CONVERT DTO → ENTITY */
    Product product = toEntity(dto);

    /* 5️⃣ HANDLE IMAGE UPDATE */
    if (imageFile != null && !imageFile.isEmpty()) {

        if (existing.getImagePath() != null) {
            s3Service.deleteImage(existing.getImagePath());
        }

        s3Service.uploadImage(
                imageFile,
                "products",
                product.getPid()
        );

        String imageUrl =
                imageUrlBuilder.buildImageUrl("products", pid);

        product.setImagePath(imageUrl);
    }

    /* 6️⃣ SAVE UPDATED PRODUCT */
    Product updated = productRepository.save(product);

    return toDTO(updated);
}



    /* ===================== FETCH IMAGE ===================== */
//    public byte[] getProductImage(Long pid) throws IOException {
//
//        Product product = productRepository.findById(pid)
//                .orElseThrow(() -> new NotFoundException("Product not found"));
//
//        if (product.getImagePath() == null) {
//            throw new NotFoundException("No image found");
//        }
//
//        File file = new File(product.getImagePath());
//        if (!file.exists()) {
//            throw new NotFoundException("Image not found on server");
//        }
//
//        return Files.readAllBytes(file.toPath());
//    }
    public byte[] getProductImage(Long pid) {

        Product product = productRepository.findById(pid)
                .orElseThrow(() ->
                        new NotFoundException("Product not found"));
        if (product.getImagePath() == null) {
            throw new ImageUploadException("Product image not uploaded yet");
        }
//        try {
//            return s3Service.getImage(product.getImagePath());
//        }
//        catch(NoSuchKeyException e){
//            throw new NotFoundException("Category not found");
//        }
        return s3Service.getImage(product.getImagePath());


    }

}
