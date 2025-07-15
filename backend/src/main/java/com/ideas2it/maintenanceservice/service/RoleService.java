package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for role-related operations.
 */
public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    Optional<RoleDTO> getRoleById(Long id);
    Optional<RoleDTO> getRoleByName(String name);
    List<RoleDTO> getAllRoles();
} 