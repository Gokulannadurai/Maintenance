package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for notification operations.
 */
public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    void markAsRead(Long id);
    Optional<NotificationDTO> getNotificationById(Long id);
    List<NotificationDTO> getUnreadNotificationsByUserId(Long userId);
} 