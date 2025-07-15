package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.NotificationDTO;
import com.ideas2it.maintenanceservice.entity.Notification;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for Notification and NotificationDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class NotificationMapper {
    /**
     * Convert Notification entity to NotificationDTO.
     */
    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) return null;
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getId());
        }
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

    /**
     * Convert NotificationDTO to Notification entity. (User must be set in service.)
     */
    public static Notification toEntity(NotificationDTO dto) {
        if (dto == null) return null;
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setMessage(dto.getMessage());
        notification.setIsRead(dto.getIsRead());
        // user must be set in service
        notification.setCreatedAt(dto.getCreatedAt());
        return notification;
    }

    /**
     * Convert a list of Notification entities to a list of NotificationDTOs.
     */
    public static List<NotificationDTO> toDTOs(List<Notification> notifications) {
        return notifications == null ? null : notifications.stream().map(NotificationMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of NotificationDTOs to a list of Notification entities.
     */
    public static List<Notification> toEntities(List<NotificationDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(NotificationMapper::toEntity).collect(Collectors.toList());
    }
} 