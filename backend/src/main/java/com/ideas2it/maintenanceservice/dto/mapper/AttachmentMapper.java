package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import com.ideas2it.maintenanceservice.entity.Attachment;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for Attachment and AttachmentDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class AttachmentMapper {
    /**
     * Convert Attachment entity to AttachmentDTO.
     */
    public static AttachmentDTO toDTO(Attachment attachment) {
        if (attachment == null) return null;
        AttachmentDTO dto = new AttachmentDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        if (attachment.getUploadedBy() != null) {
            dto.setUploadedById(attachment.getUploadedBy().getId());
            dto.setUploadedByName(attachment.getUploadedBy().getFullName());
        }
        if (attachment.getRequest() != null) {
            dto.setRequestId(attachment.getRequest().getId());
        }
        dto.setUploadedAt(attachment.getUploadedAt());
        return dto;
    }

    /**
     * Convert AttachmentDTO to Attachment entity. (Nested objects must be set by service.)
     */
    public static Attachment toEntity(AttachmentDTO dto) {
        if (dto == null) return null;
        Attachment attachment = new Attachment();
        attachment.setFileName(dto.getFileName());
        attachment.setUploadedAt(dto.getUploadedAt());
        //attachment.setUploadedBy(dto.getUploadedById());
        return attachment;
    }

    /**
     * Convert a list of Attachment entities to a list of AttachmentDTOs.
     */
    public static List<AttachmentDTO> toDTOs(List<Attachment> attachments) {
        return attachments == null ? null : attachments.stream().map(AttachmentMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of AttachmentDTOs to a list of Attachment entities.
     */
    public static List<Attachment> toEntities(List<AttachmentDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(AttachmentMapper::toEntity).collect(Collectors.toList());
    }
} 