package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for attachment operations.
 */
public interface AttachmentService {
    AttachmentDTO createAttachment(AttachmentDTO attachmentDTO);
    void deleteAttachment(Long id);
    Optional<AttachmentDTO> getAttachmentById(Long id);
    List<AttachmentDTO> getAttachmentsByRequestId(Long requestId);
} 