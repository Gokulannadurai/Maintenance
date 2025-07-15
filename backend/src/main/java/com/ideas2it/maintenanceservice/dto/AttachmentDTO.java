package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Attachment entity.
 */
@Data
public class AttachmentDTO {
    private Long id;
    private Long requestId;
    private String filePath;
    private String fileName;
    private String fileType;
    private Long uploadedById;
    private String uploadedByName;
    private LocalDateTime uploadedAt;
    private String s3key;
} 