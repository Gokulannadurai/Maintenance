package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for RequestStatusHistory entity.
 */
@Data
public class RequestStatusHistoryDTO {
    private Long id;
    private Long requestId;
    private String oldStatus;
    private String newStatus;
    private Long changedById;
    private String changedByName;
    private LocalDateTime changedAt;
    private String note;
} 