package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for AuditLog entity.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    /**
     * Find audit logs by user id.
     * @param userId the user id
     * @return list of audit logs
     */
    List<AuditLog> findByUserId(Long userId);
} 