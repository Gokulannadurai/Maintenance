package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import com.ideas2it.maintenanceservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for notification operations.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Create a new notification for a user.
     * @param notificationDTO the notification data
     * @return the created notification
     */
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        log.info("API: Creating notification for user id: {}", notificationDTO.getUserId());
        NotificationDTO created = notificationService.createNotification(notificationDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Mark a notification as read by ID.
     * @param id the notification ID
     * @return no content
     */
    @PostMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("API: Marking notification as read with id: {}", id);
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a notification by ID.
     * @param id the notification ID
     * @return the notification if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        log.info("API: Fetching notification by id: {}", id);
        Optional<NotificationDTO> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all unread notifications for a user.
     * @param userId the user ID
     * @return list of notifications
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByUserId(@RequestParam Long userId) {
        log.info("API: Fetching unread notifications for user id: {}", userId);
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUserId(userId));
    }
} 