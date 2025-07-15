package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for AuditLog entity.
 */
@Data
public class AuditLogDTO {
    private Long id;
    private Long userId;
    private String action;
    private String details;
    private LocalDateTime createdAt;
} 