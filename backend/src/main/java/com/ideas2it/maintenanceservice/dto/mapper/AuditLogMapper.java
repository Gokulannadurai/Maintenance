package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import com.ideas2it.maintenanceservice.entity.AuditLog;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for AuditLog and AuditLogDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class AuditLogMapper {
    /**
     * Convert AuditLog entity to AuditLogDTO.
     */
    public static AuditLogDTO toDTO(AuditLog auditLog) {
        if (auditLog == null) return null;
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setAction(auditLog.getAction());
        dto.setCreatedAt(auditLog.getCreatedAt());
        if (auditLog.getUser() != null) {
            dto.setUserId(auditLog.getUser().getId());
        }
        return dto;
    }

    /**
     * Convert AuditLogDTO to AuditLog entity. (User must be set in service.)
     */
    public static AuditLog toEntity(AuditLogDTO dto) {
        if (dto == null) return null;
        AuditLog auditLog = new AuditLog();
        auditLog.setId(dto.getId());
        auditLog.setAction(dto.getAction());
        auditLog.setCreatedAt(dto.getCreatedAt());
        // user must be set in service
        return auditLog;
    }

    /**
     * Convert a list of AuditLog entities to a list of AuditLogDTOs.
     */
    public static List<AuditLogDTO> toDTOs(List<AuditLog> auditLogs) {
        return auditLogs == null ? null : auditLogs.stream().map(AuditLogMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of AuditLogDTOs to a list of AuditLog entities.
     */
    public static List<AuditLog> toEntities(List<AuditLogDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(AuditLogMapper::toEntity).collect(Collectors.toList());
    }
} 