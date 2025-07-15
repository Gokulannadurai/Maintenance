package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Notification entity.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * Find unread notifications for a user.
     * @param userId the user id
     * @param isRead read status
     * @return list of notifications
     */
    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
} 