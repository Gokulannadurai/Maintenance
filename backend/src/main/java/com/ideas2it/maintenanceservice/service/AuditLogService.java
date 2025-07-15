package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import java.util.List;

/**
 * Service interface for audit log operations.
 */
public interface AuditLogService {
    AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO);
    List<AuditLogDTO> getAuditLogsByUserId(Long userId);
} 