package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import com.ideas2it.maintenanceservice.entity.Attachment;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.AttachmentRepository;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {
    @Mock AttachmentRepository attachmentRepository;
    @Mock UserRepository userRepository;
    @Mock MaintenanceRequestRepository requestRepository;
    @InjectMocks AttachmentServiceImpl attachmentService;

    private Attachment attachment;
    private AttachmentDTO attachmentDTO;
    private User user;
    private MaintenanceRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("Uploader");
        request = new MaintenanceRequest();
        request.setId(1L);
        attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFileName("file.pdf");
        attachment.setRequest(request);
        attachment.setUploadedBy(user);
        attachmentDTO = new AttachmentDTO();
        attachmentDTO.setId(1L);
        attachmentDTO.setRequestId(1L);
        attachmentDTO.setUploadedById(1L);
        attachmentDTO.setFileName("file.pdf");
    }

    @Test
    void createAttachment_success() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);
        AttachmentDTO result = attachmentService.createAttachment(attachmentDTO);
        assertNotNull(result);
        assertEquals(attachment.getFileName(), result.getFileName());
    }

    @Test
    void createAttachment_requestNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> attachmentService.createAttachment(attachmentDTO));
    }

    @Test
    void createAttachment_userNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> attachmentService.createAttachment(attachmentDTO));
    }

    @Test
    void deleteAttachment_success() {
        when(attachmentRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(attachmentRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> attachmentService.deleteAttachment(1L));
    }

    @Test
    void deleteAttachment_notFound_throws() {
        when(attachmentRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> attachmentService.deleteAttachment(1L));
    }

    @Test
    void getAttachmentById_found() {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.of(attachment));
        Optional<AttachmentDTO> result = attachmentService.getAttachmentById(1L);
        assertTrue(result.isPresent());
        assertEquals(attachment.getFileName(), result.get().getFileName());
    }

    @Test
    void getAttachmentById_notFound() {
        when(attachmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<AttachmentDTO> result = attachmentService.getAttachmentById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAttachmentsByRequestId_success() {
        when(attachmentRepository.findByRequestId(anyLong())).thenReturn(List.of(attachment));
        List<AttachmentDTO> result = attachmentService.getAttachmentsByRequestId(1L);
        assertEquals(1, result.size());
        assertEquals(attachment.getFileName(), result.get(0).getFileName());
    }
} 