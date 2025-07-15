package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import com.ideas2it.maintenanceservice.service.AttachmentService;
import com.ideas2it.maintenanceservice.service.S3FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttachmentControllerTest {
    @InjectMocks AttachmentController attachmentController;
    @Mock AttachmentService attachmentService;
    @Mock S3FileService s3FileService;
    private AttachmentDTO attachmentDTO;

    @BeforeEach
    void setUp() {
        attachmentDTO = new AttachmentDTO();
        attachmentDTO.setId(1L);
        attachmentDTO.setRequestId(1L);
        attachmentDTO.setFileName("file.pdf");
        attachmentDTO.setS3key("uuid_file.pdf");
        attachmentDTO.setFileType("application/pdf");
    }

    @Test
    void createAttachment_success() {
        when(attachmentService.createAttachment(any(AttachmentDTO.class))).thenReturn(attachmentDTO);
        ResponseEntity<AttachmentDTO> response = attachmentController.createAttachment(attachmentDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(attachmentDTO.getFileName(), response.getBody().getFileName());
    }

    @Test
    void createAttachment_requestNotFound_returnsBadRequest() {
        when(attachmentService.createAttachment(any(AttachmentDTO.class))).thenThrow(new IllegalArgumentException("Request not found"));
        assertThrows(IllegalArgumentException.class, () -> attachmentController.createAttachment(attachmentDTO));
    }

    @Test
    void deleteAttachment_success() {
        doNothing().when(attachmentService).deleteAttachment(1L);
        ResponseEntity<Void> response = attachmentController.deleteAttachment(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteAttachment_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Attachment not found")).when(attachmentService).deleteAttachment(1L);
        assertThrows(IllegalArgumentException.class, () -> attachmentController.deleteAttachment(1L));
    }

    @Test
    void getAttachmentById_found() {
        when(attachmentService.getAttachmentById(1L)).thenReturn(Optional.of(attachmentDTO));
        ResponseEntity<AttachmentDTO> response = attachmentController.getAttachmentById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(attachmentDTO.getFileName(), response.getBody().getFileName());
    }

    @Test
    void getAttachmentById_notFound() {
        when(attachmentService.getAttachmentById(1L)).thenReturn(Optional.empty());
        ResponseEntity<AttachmentDTO> response = attachmentController.getAttachmentById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAttachmentsByRequestId_success() {
        when(attachmentService.getAttachmentsByRequestId(1L)).thenReturn(List.of(attachmentDTO));
        ResponseEntity<List<AttachmentDTO>> response = attachmentController.getAttachmentsByRequestId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(attachmentDTO.getFileName(), response.getBody().get(0).getFileName());
    }

    @Test
    void uploadAttachment_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.pdf", "application/pdf", "test content".getBytes());
        when(s3FileService.uploadFile(any())).thenReturn("uuid_file.pdf");
        when(attachmentService.createAttachment(any(AttachmentDTO.class))).thenReturn(attachmentDTO);
        ResponseEntity<AttachmentDTO> response = attachmentController.uploadAttachment(file, 1L);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(attachmentDTO.getFileName(), response.getBody().getFileName());
    }

    @Test
    void uploadAttachment_emptyFile_returnsBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", "application/pdf", new byte[0]);
        ResponseEntity<AttachmentDTO> response = attachmentController.uploadAttachment(file, 1L);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void uploadAttachment_s3Error_returnsInternalServerError() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.pdf", "application/pdf", "test content".getBytes());
        when(s3FileService.uploadFile(any())).thenThrow(new RuntimeException("S3 error"));
        ResponseEntity<AttachmentDTO> response = attachmentController.uploadAttachment(file, 1L);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void downloadAttachment_success() throws Exception {
        when(attachmentService.getAttachmentById(1L)).thenReturn(Optional.of(attachmentDTO));
        when(s3FileService.downloadFile(anyString())).thenReturn("test content".getBytes());
        ResponseEntity<byte[]> response = attachmentController.downloadAttachment(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("attachment; filename=\"file.pdf\"", response.getHeaders().get("Content-Disposition").get(0));
        assertArrayEquals("test content".getBytes(), response.getBody());
    }

    @Test
    void downloadAttachment_notFound_returnsNotFound() throws Exception {
        when(attachmentService.getAttachmentById(1L)).thenReturn(Optional.empty());
        ResponseEntity<byte[]> response = attachmentController.downloadAttachment(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void downloadAttachment_s3Error_returnsInternalServerError() throws Exception {
        when(attachmentService.getAttachmentById(1L)).thenReturn(Optional.of(attachmentDTO));
        when(s3FileService.downloadFile(anyString())).thenThrow(new RuntimeException("S3 error"));
        ResponseEntity<byte[]> response = attachmentController.downloadAttachment(1L);
        assertEquals(500, response.getStatusCodeValue());
    }
} 