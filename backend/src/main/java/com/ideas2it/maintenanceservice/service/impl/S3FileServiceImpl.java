package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

/**
 * Implementation of S3FileService for file upload and download using S3Client.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileServiceImpl implements S3FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * Upload a file to S3 and return the S3 key.
     * @param file the file to upload
     * @return the S3 key
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            log.info("File uploaded to S3: {}", key);
            return key;
        } catch (IOException | S3Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to upload file");
        }
    }

    /**
     * Download a file from S3 by key.
     * @param key the S3 key
     * @return the file bytes
     */
    @Override
    public byte[] downloadFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            ResponseBytes<?> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            log.info("File downloaded from S3: {}", key);
            return objectBytes.asByteArray();
        } catch (S3Exception e) {
            log.error("Failed to download file from S3: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to download file");
        }
    }
} 