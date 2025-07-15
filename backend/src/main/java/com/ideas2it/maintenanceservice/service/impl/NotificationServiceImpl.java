package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import com.ideas2it.maintenanceservice.dto.mapper.NotificationMapper;
import com.ideas2it.maintenanceservice.entity.Notification;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.NotificationRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of NotificationService for notification operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Create a new notification for a user.
     * @param notificationDTO the notification data to create
     * @return the created NotificationDTO
     * @throws IllegalArgumentException if user not found
     */
    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        log.info("Creating notification for user id: {}", notificationDTO.getUserId());
        User user = userRepository.findById(notificationDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Notification notification = NotificationMapper.toEntity(notificationDTO);
        notification.setUser(user);
        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toDTO(saved);
    }

    /**
     * Mark a notification as read by ID.
     * @param id the notification ID
     * @throws IllegalArgumentException if notification not found
     */
    @Override
    public void markAsRead(Long id) {
        log.info("Marking notification as read with id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Get a notification by ID.
     * @param id the notification ID
     * @return Optional of NotificationDTO if found
     */
    @Override
    public Optional<NotificationDTO> getNotificationById(Long id) {
        log.info("Fetching notification by id: {}", id);
        return notificationRepository.findById(id).map(NotificationMapper::toDTO);
    }

    /**
     * Get all unread notifications for a user.
     * @param userId the user ID
     * @return list of NotificationDTOs
     */
    @Override
    public List<NotificationDTO> getUnreadNotificationsByUserId(Long userId) {
        log.info("Fetching unread notifications for user id: {}", userId);
        return NotificationMapper.toDTOs(notificationRepository.findByUserIdAndIsRead(userId, false));
    }
} 