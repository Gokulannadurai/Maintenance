package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import com.ideas2it.maintenanceservice.dto.mapper.RoleMapper;
import com.ideas2it.maintenanceservice.entity.Role;
import com.ideas2it.maintenanceservice.repository.RoleRepository;
import com.ideas2it.maintenanceservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of RoleService for role-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * Create a new role in the system.
     * @param roleDTO the role data to create
     * @return the created RoleDTO
     * @throws IllegalArgumentException if role name already exists
     */
    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        log.info("Creating role with name: {}", roleDTO.getName());
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Role name already exists");
        }
        Role role = RoleMapper.toEntity(roleDTO);
        Role saved = roleRepository.save(role);
        return RoleMapper.toDTO(saved);
    }

    /**
     * Update an existing role's details.
     * @param id the role ID
     * @param roleDTO the updated role data
     * @return the updated RoleDTO
     * @throws IllegalArgumentException if role not found
     */
    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        log.info("Updating role with id: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        Role updated = roleRepository.save(role);
        return RoleMapper.toDTO(updated);
    }

    /**
     * Delete a role by ID.
     * @param id the role ID
     * @throws IllegalArgumentException if role not found
     */
    @Override
    public void deleteRole(Long id) {
        log.info("Deleting role with id: {}", id);
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    /**
     * Get a role by ID.
     * @param id the role ID
     * @return Optional of RoleDTO if found
     */
    @Override
    public Optional<RoleDTO> getRoleById(Long id) {
        log.info("Fetching role by id: {}", id);
        return roleRepository.findById(id).map(RoleMapper::toDTO);
    }

    /**
     * Get a role by name.
     * @param name the role name
     * @return Optional of RoleDTO if found
     */
    @Override
    public Optional<RoleDTO> getRoleByName(String name) {
        log.info("Fetching role by name: {}", name);
        return roleRepository.findByName(name).map(RoleMapper::toDTO);
    }

    /**
     * Get all roles in the system.
     * @return list of RoleDTOs
     */
    @Override
    public List<RoleDTO> getAllRoles() {
        log.info("Fetching all roles");
        return RoleMapper.toDTOs(roleRepository.findAll());
    }
} 