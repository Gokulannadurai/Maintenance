package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Attachment entity.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    /**
     * Find attachments by request id.
     * @param requestId the maintenance request id
     * @return list of attachments
     */
    List<Attachment> findByRequestId(Long requestId);
} 