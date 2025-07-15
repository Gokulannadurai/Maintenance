package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for MaintenanceRequest entity.
 */
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    /**
     * Find requests by status.
     * @param status the request status
     * @return list of requests
     */
    List<MaintenanceRequest> findByStatus(MaintenanceRequest.Status status);

    /**
     * Find requests assigned to a user.
     * @param assignedTo the assigned user
     * @return list of requests
     */
    List<MaintenanceRequest> findByAssignedTo(User assignedTo);
} 