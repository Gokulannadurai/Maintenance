package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.service.impl.S3FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3FileServiceTest {
    @Mock S3Client s3Client;
    @InjectMocks S3FileServiceImpl s3FileService;

    @BeforeEach
    void setUp() {
        // Set bucketName via reflection since @Value is not processed in unit tests
        try {
            java.lang.reflect.Field field = S3FileServiceImpl.class.getDeclaredField("bucketName");
            field.setAccessible(true);
            field.set(s3FileService, "test-bucket");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void uploadFile_success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "file.pdf", "application/pdf", "test content".getBytes());
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(PutObjectResponse.builder().build());
        String key = s3FileService.uploadFile(file);
        assertNotNull(key);
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void uploadFile_s3Exception_throws() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "file.pdf", "application/pdf", "test content".getBytes());
        doThrow(S3Exception.builder().message("S3 error").build()).when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertThrows(IllegalArgumentException.class, () -> s3FileService.uploadFile(file));
    }

    @Test
    void downloadFile_success() {
        byte[] content = "test content".getBytes();
        ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);
        when(responseBytes.asByteArray()).thenReturn(content);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);
        byte[] result = s3FileService.downloadFile("key");
        assertArrayEquals(content, result);
    }

    @Test
    void downloadFile_s3Exception_throws() {
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenThrow(S3Exception.builder().message("S3 error").build());
        assertThrows(IllegalArgumentException.class, () -> s3FileService.downloadFile("key"));
    }
} 