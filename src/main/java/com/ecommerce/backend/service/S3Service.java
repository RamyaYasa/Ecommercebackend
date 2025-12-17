package com.ecommerce.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseBytes;
import com.ecommerce.backend.exception.ImageUploadException;

import java.io.IOException;

@Service
public class S3Service {
    @Value("${app.base-url}")
    private String baseUrl;

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /* ---------- UPLOAD IMAGE ---------- */
    public String uploadImage(MultipartFile file,String tableName,Long id) throws IOException {

        String imageKey =
//                "subcategory/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
                baseUrl+"/api/admin/" + tableName + "/image/" +id;



        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imageKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request,
                RequestBody.fromBytes(file.getBytes()));

        return imageKey; // store this in DB
    }


    /* ---------- DOWNLOAD IMAGE ---------- */
//    public byte[] getImage(String imageKey) {
//
//        ResponseBytes<GetObjectResponse> response =
//                s3Client.getObjectAsBytes(
//                        GetObjectRequest.builder()
//                                .bucket(bucketName)
//                                .key(imageKey)
//                                .build()
//                );
//
//        return response.asByteArray();
//    }
    public byte[] getImage(String imageKey) {

        if (imageKey == null || imageKey.trim().isEmpty()) {
            throw new ImageUploadException("Image key is null or empty");
        }

        try {
            return s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(imageKey)
                            .build()
            ).asByteArray();

        } catch (NoSuchKeyException e) {
            throw new ImageUploadException("Image not found in S3");
        }
    }


    /* ---------- DELETE IMAGE ---------- */
    public void deleteImage(String imageKey) {

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(imageKey)
                        .build()
        );
    }
}


