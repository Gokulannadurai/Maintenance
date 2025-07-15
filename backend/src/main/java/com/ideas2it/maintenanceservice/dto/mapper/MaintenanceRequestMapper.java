package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for MaintenanceRequest and MaintenanceRequestDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class MaintenanceRequestMapper {
    /**
     * Convert MaintenanceRequest entity to MaintenanceRequestDTO.
     */
    public static MaintenanceRequestDTO toDTO(MaintenanceRequest request) {
        if (request == null) return null;
        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus().toString());
        dto.setPriority(request.getPriority().toString());
        if (request.getRequester() != null) {
            dto.setRequesterId(request.getRequester().getId());
            dto.setRequesterName(request.getRequester().getFullName());
        }
        if (request.getAssignedTo() != null) {
            dto.setAssignedToId(request.getAssignedTo().getId());
            dto.setAssignedToName(request.getAssignedTo().getFullName());
        }
        if (request.getCategory() != null) {
            dto.setCategoryId(request.getCategory().getId());
            dto.setCategoryName(request.getCategory().getName());
        }
        if (request.getLocation() != null) {
            dto.setLocationId(request.getLocation().getId());
            dto.setLocationName(request.getLocation().getName());
        }
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }

    /**
     * Convert MaintenanceRequestDTO to MaintenanceRequest entity. (Nested objects must be set by service.)
     */
    public static MaintenanceRequest toEntity(MaintenanceRequestDTO dto) {
        if (dto == null) return null;
        MaintenanceRequest request = new MaintenanceRequest();
        request.setId(dto.getId());
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setStatus(MaintenanceRequest.Status.valueOf(dto.getStatus()));
        request.setPriority(MaintenanceRequest.Priority.valueOf(dto.getPriority()));
        // requester, assignedTo, category, location must be set in service
        request.setCreatedAt(dto.getCreatedAt());
        request.setUpdatedAt(dto.getUpdatedAt());
        return request;
    }

    /**
     * Convert a list of MaintenanceRequest entities to a list of MaintenanceRequestDTOs.
     */
    public static List<MaintenanceRequestDTO> toDTOs(List<MaintenanceRequest> requests) {
        return requests == null ? null : requests.stream().map(MaintenanceRequestMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of MaintenanceRequestDTOs to a list of MaintenanceRequest entities.
     */
    public static List<MaintenanceRequest> toEntities(List<MaintenanceRequestDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(MaintenanceRequestMapper::toEntity).collect(Collectors.toList());
    }
} 