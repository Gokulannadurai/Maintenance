package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import com.ideas2it.maintenanceservice.entity.RequestStatusHistory;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for RequestStatusHistory and RequestStatusHistoryDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class RequestStatusHistoryMapper {
    /**
     * Convert RequestStatusHistory entity to RequestStatusHistoryDTO.
     */
    public static RequestStatusHistoryDTO toDTO(RequestStatusHistory statusHistory) {
        if (statusHistory == null) return null;
        RequestStatusHistoryDTO dto = new RequestStatusHistoryDTO();
        dto.setId(statusHistory.getId());
        dto.setNewStatus(statusHistory.getNewStatus().toString());
        dto.setOldStatus(statusHistory.getOldStatus().toString());
        dto.setChangedAt(statusHistory.getChangedAt());
        if (statusHistory.getChangedBy() != null) {
            dto.setChangedById(statusHistory.getChangedBy().getId());
            dto.setChangedByName(statusHistory.getChangedBy().getFullName());
        }
        if (statusHistory.getRequest() != null) {
            dto.setRequestId(statusHistory.getRequest().getId());
        }
        return dto;
    }

    /**
     * Convert RequestStatusHistoryDTO to RequestStatusHistory entity. (Nested objects must be set by service.)
     */
    public static RequestStatusHistory toEntity(RequestStatusHistoryDTO dto) {
        if (dto == null) return null;
        RequestStatusHistory statusHistory = new RequestStatusHistory();
        statusHistory.setId(dto.getId());
        statusHistory.setOldStatus(RequestStatusHistory.Status.valueOf(dto.getOldStatus()));
        statusHistory.setNewStatus(RequestStatusHistory.Status.valueOf(dto.getNewStatus()));
        statusHistory.setChangedAt(dto.getChangedAt());
        // changedBy, request must be set in service
        return statusHistory;
    }

    /**
     * Convert a list of RequestStatusHistory entities to a list of RequestStatusHistoryDTOs.
     */
    public static List<RequestStatusHistoryDTO> toDTOs(List<RequestStatusHistory> statusHistories) {
        return statusHistories == null ? null : statusHistories.stream().map(RequestStatusHistoryMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of RequestStatusHistoryDTOs to a list of RequestStatusHistory entities.
     */
    public static List<RequestStatusHistory> toEntities(List<RequestStatusHistoryDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(RequestStatusHistoryMapper::toEntity).collect(Collectors.toList());
    }
} 