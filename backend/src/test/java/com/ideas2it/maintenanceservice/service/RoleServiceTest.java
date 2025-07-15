package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import com.ideas2it.maintenanceservice.entity.Role;
import com.ideas2it.maintenanceservice.repository.RoleRepository;
import com.ideas2it.maintenanceservice.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock RoleRepository roleRepository;
    @InjectMocks RoleServiceImpl roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setDescription("Admin role");

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("ADMIN");
        roleDTO.setDescription("Admin role");
    }

    @Test
    void createRole_success() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        RoleDTO result = roleService.createRole(roleDTO);
        assertNotNull(result);
        assertEquals(role.getName(), result.getName());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_duplicateName_throws() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(roleDTO));
    }

    @Test
    void updateRole_success() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        RoleDTO result = roleService.updateRole(1L, roleDTO);
        assertNotNull(result);
        assertEquals(role.getName(), result.getName());
    }

    @Test
    void updateRole_notFound_throws() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> roleService.updateRole(1L, roleDTO));
    }

    @Test
    void deleteRole_success() {
        when(roleRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(roleRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> roleService.deleteRole(1L));
    }

    @Test
    void deleteRole_notFound_throws() {
        when(roleRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> roleService.deleteRole(1L));
    }

    @Test
    void getRoleById_found() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        Optional<RoleDTO> result = roleService.getRoleById(1L);
        assertTrue(result.isPresent());
        assertEquals(role.getName(), result.get().getName());
    }

    @Test
    void getRoleById_notFound() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<RoleDTO> result = roleService.getRoleById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRoleByName_found() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        Optional<RoleDTO> result = roleService.getRoleByName("ADMIN");
        assertTrue(result.isPresent());
        assertEquals(role.getName(), result.get().getName());
    }

    @Test
    void getRoleByName_notFound() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        Optional<RoleDTO> result = roleService.getRoleByName("ADMIN");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRoles_success() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        List<RoleDTO> result = roleService.getAllRoles();
        assertEquals(1, result.size());
        assertEquals(role.getName(), result.get(0).getName());
    }
} 