package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import com.ideas2it.maintenanceservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for role-related operations.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    /**
     * Create a new role.
     * @param roleDTO the role data
     * @return the created role
     */
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        log.info("API: Creating role with name: {}", roleDTO.getName());
        RoleDTO created = roleService.createRole(roleDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing role.
     * @param id the role ID
     * @param roleDTO the updated role data
     * @return the updated role
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        log.info("API: Updating role with id: {}", id);
        RoleDTO updated = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a role by ID.
     * @param id the role ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("API: Deleting role with id: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a role by ID.
     * @param id the role ID
     * @return the role if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        log.info("API: Fetching role by id: {}", id);
        Optional<RoleDTO> role = roleService.getRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get a role by name.
     * @param name the role name
     * @return the role if found
     */
    @GetMapping("/by-name")
    public ResponseEntity<RoleDTO> getRoleByName(@RequestParam String name) {
        log.info("API: Fetching role by name: {}", name);
        Optional<RoleDTO> role = roleService.getRoleByName(name);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all roles.
     * @return list of roles
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        log.info("API: Fetching all roles");
        return ResponseEntity.ok(roleService.getAllRoles());
    }
} 