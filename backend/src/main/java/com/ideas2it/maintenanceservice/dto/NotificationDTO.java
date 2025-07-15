package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Notification entity.
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
} 