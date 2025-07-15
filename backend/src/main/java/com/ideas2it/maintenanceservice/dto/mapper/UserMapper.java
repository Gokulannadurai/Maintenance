package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import com.ideas2it.maintenanceservice.entity.Role;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.RoleRepository;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Manual mapper for User and UserDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class UserMapper {
    /**
     * Convert User entity to UserDTO.
     */
    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getIsActive());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        }
        return dto;
    }

    /**
     * Convert UserDTO to User entity. Requires RoleRepository to resolve role names.
     */
    public static User toEntity(UserDTO dto, RoleRepository roleRepository) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setIsActive(dto.getIsActive());
        if (dto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.getRoles()) {
                roleRepository.findByName(roleName).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }
        return user;
    }

    /**
     * Convert a list of User entities to a list of UserDTOs.
     */
    public static List<UserDTO> toDTOs(List<User> users) {
        return users == null ? null : users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of UserDTOs to a list of User entities. Requires RoleRepository.
     */
    public static List<User> toEntities(List<UserDTO> dtos, RoleRepository roleRepository) {
        return dtos == null ? null : dtos.stream().map(dto -> toEntity(dto, roleRepository)).collect(Collectors.toList());
    }
} 