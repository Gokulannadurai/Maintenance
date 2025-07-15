package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import com.ideas2it.maintenanceservice.service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuditLogControllerTest {
    @InjectMocks AuditLogController auditLogController;
    @Mock AuditLogService auditLogService;
    private AuditLogDTO auditLogDTO;

    @BeforeEach
    void setUp() {
        auditLogDTO = new AuditLogDTO();
        auditLogDTO.setId(1L);
        auditLogDTO.setUserId(1L);
        auditLogDTO.setAction("LOGIN");
        auditLogDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createAuditLog_success() {
        when(auditLogService.createAuditLog(any(AuditLogDTO.class))).thenReturn(auditLogDTO);
        var response = auditLogController.createAuditLog(auditLogDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(auditLogDTO.getAction(), response.getBody().getAction());
    }

    @Test
    void createAuditLog_userNotFound_returnsBadRequest() {
        when(auditLogService.createAuditLog(any(AuditLogDTO.class))).thenThrow(new IllegalArgumentException("User not found"));
        assertThrows(IllegalArgumentException.class, () -> auditLogController.createAuditLog(auditLogDTO));
    }

    @Test
    void getAuditLogsByUserId_success() {
        when(auditLogService.getAuditLogsByUserId(1L)).thenReturn(List.of(auditLogDTO));
        var response = auditLogController.getAuditLogsByUserId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(auditLogDTO.getAction(), response.getBody().get(0).getAction());
    }
} 