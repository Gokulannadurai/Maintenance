package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import com.ideas2it.maintenanceservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {
    @InjectMocks NotificationController notificationController;
    @Mock NotificationService notificationService;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(1L);
        notificationDTO.setMessage("Test notification");
        notificationDTO.setIsRead(false);
    }

    @Test
    void createNotification_success() {
        when(notificationService.createNotification(any(NotificationDTO.class))).thenReturn(notificationDTO);
        var response = notificationController.createNotification(notificationDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(notificationDTO.getMessage(), response.getBody().getMessage());
    }

    @Test
    void createNotification_userNotFound_returnsBadRequest() {
        when(notificationService.createNotification(any(NotificationDTO.class))).thenThrow(new IllegalArgumentException("User not found"));
        assertThrows(IllegalArgumentException.class, () -> notificationController.createNotification(notificationDTO));
    }

    @Test
    void markAsRead_success() {
        doNothing().when(notificationService).markAsRead(1L);
        var response = notificationController.markAsRead(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void markAsRead_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Notification not found")).when(notificationService).markAsRead(1L);
        assertThrows(IllegalArgumentException.class, () -> notificationController.markAsRead(1L));
    }

    @Test
    void getNotificationById_found() {
        when(notificationService.getNotificationById(1L)).thenReturn(Optional.of(notificationDTO));
        var response = notificationController.getNotificationById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(notificationDTO.getMessage(), response.getBody().getMessage());
    }

    @Test
    void getNotificationById_notFound() {
        when(notificationService.getNotificationById(1L)).thenReturn(Optional.empty());
        var response = notificationController.getNotificationById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getUnreadNotificationsByUserId_success() {
        when(notificationService.getUnreadNotificationsByUserId(1L)).thenReturn(List.of(notificationDTO));
        var response = notificationController.getUnreadNotificationsByUserId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(notificationDTO.getMessage(), response.getBody().get(0).getMessage());
    }
} 