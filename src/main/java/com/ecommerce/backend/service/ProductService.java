//package com.ecommerce.backend.service;
//import com.ecommerce.backend.dto.CategoryDTO;
//import com.ecommerce.backend.dto.ProductDTO;
//import com.ecommerce.backend.entity.Category;
//import com.ecommerce.backend.entity.Product;
//import com.ecommerce.backend.exception.AlreadyExistsException;
//import com.ecommerce.backend.exception.NotFoundException;
//import com.ecommerce.backend.repository.CategoryRepository;
//import com.ecommerce.backend.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.stream.Collectors;
//import com.ecommerce.backend.exception.ImageUploadException;
//import com.ecommerce.backend.exception.InvalidRequestException;
//import org.springframework.web.multipart.MultipartFile;
//import java.nio.file.Files;
//import java.io.File;
//import java.io.IOException;
//
//@Service
//public class ProductService {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
////    public ProductService(ProductRepository productRepository,
////                          CategoryRepository categoryRepository) {
////        this.productRepository = productRepository;
////        this.categoryRepository = categoryRepository;
////    }
//
//    private Product toEntity(ProductDTO dto) {
//        Product p = new Product();
//
//        p.setPid(dto.getPid());
//        p.setPname(dto.getPname());
//        p.setDescription(dto.getDescription());
//        p.setPrice(dto.getPrice());
//        p.setStockQuantity(dto.getStockQuantity());
//        p.setImagePath(dto.getImagePath());
//        p.setActualPrice(dto.getActualPrice());
//        p.setRating(dto.getRating());
//        p.setQty(dto.getQty());
//        p.setDiscount(dto.getDiscount());
//
//        if (dto.getCategory() != null) {
//            Category c = categoryRepository.findById(dto.getCategory().getCid())
//                    .orElse(null);
//            p.setCategory(c);
//        }
//
//        return p;
//    }
//
//    private ProductDTO toDTO(Product product) {
//
//        CategoryDTO categoryDTO = null;
//        if (product.getCategory() != null) {
//            categoryDTO = new CategoryDTO(
//                    product.getCategory().getCid(),
//                    product.getCategory().getCname(),
//                    product.getCategory().getCphoto()
//            );
//        }
//
//        return new ProductDTO(
//                product.getPid(),
//                product.getPname(),
//                product.getDescription(),
//                product.getPrice(),
//                product.getStockQuantity(),
//                product.getImagePath(),
//                product.getActualPrice(),
//                product.getDiscount(),
//                product.getRating(),
//                product.getQty(),
//                categoryDTO
//        );
//    }
//
//
//    public List<ProductDTO> getAllProducts() {
//        List<ProductDTO> pro= productRepository.findAll()
//                .stream().map(this::toDTO)
//                .collect(Collectors.toList());
//        if (pro.isEmpty()) throw new NotFoundException("There are no products exists");
//        else return pro;
//    }
//
//    public List<ProductDTO> getProductsByCategory(Long cid) {
//        List<ProductDTO> pro= productRepository.findByCategoryCid(cid)
//                .stream().map(this::toDTO)
//                .collect(Collectors.toList());
//        if (!pro.isEmpty()){
//            return pro;
//        }
//        else{
//            throw new NotFoundException("Category id "+cid+" doesn't exists in products");
//        }
//    }
//    public ProductDTO getProduct(Long pid) {
//        return productRepository.findById(pid)
//                .map(this::toDTO)
//                .orElseThrow(() -> new NotFoundException("Product id "+pid+" doesn't exists"));
//    }
//
//    public void deleteProduct(Long pid) {
//        if (!productRepository.existsById(pid)) {
//            throw new NotFoundException("Product id with "+pid+" doesn't exists to delete");
//        }
//        productRepository.deleteById(pid);
//    }
//
//public ProductDTO addProductWithImage(ProductDTO dto, MultipartFile file) {
//
//    try {
//        if (file == null || file.isEmpty()) {
//            throw new InvalidRequestException("Image file is missing or empty.");
//        }
//        String uploadDir = "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
//        File folder = new File(uploadDir);
//
//        if (!folder.exists() && !folder.mkdirs()) {
//            throw new ImageUploadException("Failed to create image upload directory.");
//        }
//
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        String filePath = uploadDir + fileName;
//
//        try {
//            file.transferTo(new File(filePath));
//        } catch (IOException e) {
//            throw new ImageUploadException("Failed to upload image. " + e.getMessage());
//        }
//
//        dto.setImagePath(filePath);
//
//        double actualPrice = dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0);
//        dto.setActualPrice(actualPrice);
//        Product existingProduct = productRepository.findByPid(dto.getPid());
//        if (existingProduct != null) {
//            throw new AlreadyExistsException("Product id with " + dto.getPid() + " already exists");
//        } else {
//
//            Product saved = productRepository.save(toEntity(dto));
//            return toDTO(saved);
//        }
//    } catch (InvalidRequestException | ImageUploadException ex) {
//        throw ex; // rethrow your custom exceptions
//    } catch (Exception ex) {
//        throw new ImageUploadException("Unexpected error while adding product: " + ex.getMessage());
//    }
//
//}
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
//            product.setQty(dto.getQty());
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
//    public byte[] getProductImage(Long pid) throws IOException {
//        Product product = productRepository.findById(pid)
//                .orElseThrow(() -> new NotFoundException("Product not found"));
//
//        String imagePath = product.getImagePath();
//
//        if (imagePath == null) {
//            throw new NotFoundException("No image stored for this product");
//        }
//
//        File file = new File(imagePath);
//
//        if (!file.exists()) {
//            throw new NotFoundException("Image file not found on the server");
//        }
//
//        return Files.readAllBytes(file.toPath());
//    }
//}
//
//



package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.CategoryDTO;
import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.dto.SubCategoryDTO;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.exception.*;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        p.setQty(dto.getQty());
        p.setDiscount(dto.getDiscount());

        if (dto.getCategory() != null) {
            Category c = categoryRepository.findById(dto.getCategory().getCid())
                    .orElse(null);
            p.setCategory(c);
        }

        return p;
    }

    /* ===================== ENTITY → DTO ===================== */
    private ProductDTO toDTO(Product product) {

        CategoryDTO categoryDTO = null;

        if (product.getCategory() != null) {

            SubCategoryDTO subDTO = null;
            if (product.getCategory().getSubCategory() != null) {
                subDTO = new SubCategoryDTO(
                        product.getCategory().getSubCategory().getSid(),
                        product.getCategory().getSubCategory().getSname(),
                        product.getCategory().getSubCategory().getSphoto()
                );
            }

            categoryDTO = new CategoryDTO(
                    product.getCategory().getCid(),
                    product.getCategory().getCname(),
                    product.getCategory().getCphoto(),
                    subDTO
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
                product.getQty(),
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
    public void deleteProduct(Long pid) {
        if (!productRepository.existsById(pid)) {
            throw new NotFoundException("Product id " + pid + " doesn't exist");
        }
        productRepository.deleteById(pid);
    }

    /* ===================== ADD WITH IMAGE ===================== */
    public ProductDTO addProductWithImage(ProductDTO dto, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Image file is missing");
        }

        String uploadDir = "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
        File folder = new File(uploadDir);

        if (!folder.exists() && !folder.mkdirs()) {
            throw new ImageUploadException("Failed to create image directory");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed");
        }

        dto.setImagePath(filePath);
        dto.setActualPrice(dto.getPrice() - (dto.getPrice() * dto.getDiscount() / 100.0));

        if (productRepository.findByPid(dto.getPid()) != null) {
            throw new AlreadyExistsException("Product id already exists");
        }

        Product saved = productRepository.save(toEntity(dto));
        return toDTO(saved);
    }
    public ProductDTO updateProductWithImage(Long pid, ProductDTO dto, MultipartFile imageFile) {

        try {
            Product product = productRepository.findById(pid)
                    .orElseThrow(() -> new NotFoundException("Product id " + pid + " does not exist."));

            product.setPname(dto.getPname());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setRating(dto.getRating());
            product.setQty(dto.getQty());
            product.setStockQuantity(dto.getStockQuantity());
            product.setDiscount(dto.getDiscount());

            double discountAmount = (dto.getPrice() * dto.getDiscount()) / 100.0;
            product.setActualPrice(dto.getPrice() - discountAmount);

            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "C:/Users/gumma/OneDrive/Desktop/Temporary-storage/";
                File folder = new File(uploadDir);

                if (!folder.exists() && !folder.mkdirs()) {
                    throw new ImageUploadException("Failed to create upload folder.");
                }

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String filePath = uploadDir + fileName;

                try {
                    imageFile.transferTo(new File(filePath));
                } catch (IOException e) {
                    throw new ImageUploadException("Image upload failed: " + e.getMessage());
                }

                product.setImagePath(filePath);
            }

            Category category = categoryRepository.findById(dto.getCategory().getCid())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategory().getCid()));

            product.setCategory(category);

            Product saved = productRepository.save(product);
            return toDTO(saved);

        } catch (NotFoundException | ImageUploadException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidRequestException("Unexpected error while updating product: " + ex.getMessage());
        }
    }

    /* ===================== FETCH IMAGE ===================== */
    public byte[] getProductImage(Long pid) throws IOException {

        Product product = productRepository.findById(pid)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (product.getImagePath() == null) {
            throw new NotFoundException("No image found");
        }

        File file = new File(product.getImagePath());
        if (!file.exists()) {
            throw new NotFoundException("Image not found on server");
        }

        return Files.readAllBytes(file.toPath());
    }
}
