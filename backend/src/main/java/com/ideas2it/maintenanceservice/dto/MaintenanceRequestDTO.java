package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for MaintenanceRequest entity.
 */
@Data
public class MaintenanceRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long requesterId;
    private String requesterName;
    private Long assignedToId;
    private String assignedToName;
    private Long categoryId;
    private String categoryName;
    private Long locationId;
    private String locationName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 