package com.ideas2it.maintenanceservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for S3 file upload and download operations.
 */
public interface S3FileService {
    /**
     * Upload a file to S3 and return the S3 key or URL.
     * @param file the file to upload
     * @return the S3 key or URL
     */
    String uploadFile(MultipartFile file);

    /**
     * Download a file from S3 by key.
     * @param key the S3 key
     * @return the file bytes
     */
    byte[] downloadFile(String key);
} 