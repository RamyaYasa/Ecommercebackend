//package com.ecommerce.backend.service;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.s3.S3Client;
//import lombok.Value;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.io.IOException;
//import java.io.;
//
//
//@Service
//public class S3Service {
//    @Autowired
//    private S3Client s3Client;
//
//    @Value("${aws.bucket.name}")
//    private String bucketName;
//    public void uploadFile(Multipart file) throws IOException {
//        s3Client.putObject(PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(file.getOriginalFilename())
//                .build(),RequestBody.frombytes(file.getBytes());
//    }
//    public byte[] downloadFile(String key){
//        ResponseBytes<GetObjectResponse> objectAsBytes =s3Client.getObjectAsBytes(GetObjectRequest
//                .bucket(bucketName)
//                .key(key)
//                .build());
//        return objectAsBytes.asByteArray();
//
//
//    }
//
//}
