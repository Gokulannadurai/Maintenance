package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.RequestStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for RequestStatusHistory entity.
 */
public interface RequestStatusHistoryRepository extends JpaRepository<RequestStatusHistory, Long> {
    /**
     * Find status history by request id.
     * @param requestId the maintenance request id
     * @return list of status history
     */
    List<RequestStatusHistory> findByRequestId(Long requestId);
} 