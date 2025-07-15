package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import com.ideas2it.maintenanceservice.entity.Notification;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.NotificationRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock NotificationRepository notificationRepository;
    @Mock UserRepository userRepository;
    @InjectMocks NotificationServiceImpl notificationService;

    private Notification notification;
    private NotificationDTO notificationDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setMessage("Test notification");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(1L);
        notificationDTO.setMessage("Test notification");
        notificationDTO.setIsRead(false);
        notificationDTO.setCreatedAt(notification.getCreatedAt());
    }

    @Test
    void createNotification_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        NotificationDTO result = notificationService.createNotification(notificationDTO);
        assertNotNull(result);
        assertEquals(notification.getMessage(), result.getMessage());
    }

    @Test
    void createNotification_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(notificationDTO));
    }

    @Test
    void markAsRead_success() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        assertDoesNotThrow(() -> notificationService.markAsRead(1L));
    }

    @Test
    void markAsRead_notFound_throws() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> notificationService.markAsRead(1L));
    }

    @Test
    void getNotificationById_found() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        Optional<NotificationDTO> result = notificationService.getNotificationById(1L);
        assertTrue(result.isPresent());
        assertEquals(notification.getMessage(), result.get().getMessage());
    }

    @Test
    void getNotificationById_notFound() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<NotificationDTO> result = notificationService.getNotificationById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUnreadNotificationsByUserId_success() {
        when(notificationRepository.findByUserIdAndIsRead(anyLong(), eq(false))).thenReturn(List.of(notification));
        List<NotificationDTO> result = notificationService.getUnreadNotificationsByUserId(1L);
        assertEquals(1, result.size());
        assertEquals(notification.getMessage(), result.get(0).getMessage());
    }
} 