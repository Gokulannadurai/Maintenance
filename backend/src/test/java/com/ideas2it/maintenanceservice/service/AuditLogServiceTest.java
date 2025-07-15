package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import com.ideas2it.maintenanceservice.entity.AuditLog;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.AuditLogRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {
    @Mock AuditLogRepository auditLogRepository;
    @Mock UserRepository userRepository;
    @InjectMocks AuditLogServiceImpl auditLogService;

    private AuditLog auditLog;
    private AuditLogDTO auditLogDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        auditLog = new AuditLog();
        auditLog.setId(1L);
        auditLog.setUser(user);
        auditLog.setAction("LOGIN");
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLogDTO = new AuditLogDTO();
        auditLogDTO.setId(1L);
        auditLogDTO.setUserId(1L);
        auditLogDTO.setAction("LOGIN");
        auditLogDTO.setCreatedAt(auditLog.getCreatedAt());
    }

    @Test
    void createAuditLog_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);
        AuditLogDTO result = auditLogService.createAuditLog(auditLogDTO);
        assertNotNull(result);
        assertEquals(auditLog.getAction(), result.getAction());
    }

    @Test
    void createAuditLog_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> auditLogService.createAuditLog(auditLogDTO));
    }

    @Test
    void getAuditLogsByUserId_success() {
        when(auditLogRepository.findByUserId(anyLong())).thenReturn(List.of(auditLog));
        List<AuditLogDTO> result = auditLogService.getAuditLogsByUserId(1L);
        assertEquals(1, result.size());
        assertEquals(auditLog.getAction(), result.get(0).getAction());
    }
} 