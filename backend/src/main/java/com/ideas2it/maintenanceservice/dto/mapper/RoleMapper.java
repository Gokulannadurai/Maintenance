package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import com.ideas2it.maintenanceservice.entity.Role;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for Role and RoleDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class RoleMapper {
    /**
     * Convert Role entity to RoleDTO.
     */
    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        return dto;
    }

    /**
     * Convert RoleDTO to Role entity.
     */
    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return role;
    }

    /**
     * Convert a list of Role entities to a list of RoleDTOs.
     */
    public static List<RoleDTO> toDTOs(List<Role> roles) {
        return roles == null ? null : roles.stream().map(RoleMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of RoleDTOs to a list of Role entities.
     */
    public static List<Role> toEntities(List<RoleDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(RoleMapper::toEntity).collect(Collectors.toList());
    }
} 