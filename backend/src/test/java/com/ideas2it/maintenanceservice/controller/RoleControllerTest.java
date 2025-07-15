package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.RoleDTO;
import com.ideas2it.maintenanceservice.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    @InjectMocks RoleController roleController;
    @Mock RoleService roleService;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("ADMIN");
        roleDTO.setDescription("Admin role");
    }

    @Test
    void createRole_success() {
        when(roleService.createRole(any(RoleDTO.class))).thenReturn(roleDTO);
        var response = roleController.createRole(roleDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roleDTO.getName(), response.getBody().getName());
    }

    @Test
    void createRole_duplicateName_returnsBadRequest() {
        when(roleService.createRole(any(RoleDTO.class))).thenThrow(new IllegalArgumentException("Role name already exists"));
        assertThrows(IllegalArgumentException.class, () -> roleController.createRole(roleDTO));
    }

    @Test
    void updateRole_success() {
        when(roleService.updateRole(eq(1L), any(RoleDTO.class))).thenReturn(roleDTO);
        var response = roleController.updateRole(1L, roleDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roleDTO.getName(), response.getBody().getName());
    }

    @Test
    void updateRole_notFound_returnsBadRequest() {
        when(roleService.updateRole(eq(1L), any(RoleDTO.class))).thenThrow(new IllegalArgumentException("Role not found"));
        assertThrows(IllegalArgumentException.class, () -> roleController.updateRole(1L, roleDTO));
    }

    @Test
    void deleteRole_success() {
        doNothing().when(roleService).deleteRole(1L);
        var response = roleController.deleteRole(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteRole_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Role not found")).when(roleService).deleteRole(1L);
        assertThrows(IllegalArgumentException.class, () -> roleController.deleteRole(1L));
    }

    @Test
    void getRoleById_found() {
        when(roleService.getRoleById(1L)).thenReturn(Optional.of(roleDTO));
        var response = roleController.getRoleById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roleDTO.getName(), response.getBody().getName());
    }

    @Test
    void getRoleById_notFound() {
        when(roleService.getRoleById(1L)).thenReturn(Optional.empty());
        var response = roleController.getRoleById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getRoleByName_found() {
        when(roleService.getRoleByName(anyString())).thenReturn(Optional.of(roleDTO));
        var response = roleController.getRoleByName("ADMIN");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roleDTO.getName(), response.getBody().getName());
    }

    @Test
    void getRoleByName_notFound() {
        when(roleService.getRoleByName(anyString())).thenReturn(Optional.empty());
        var response = roleController.getRoleByName("ADMIN");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllRoles_success() {
        when(roleService.getAllRoles()).thenReturn(List.of(roleDTO));
        var response = roleController.getAllRoles();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(roleDTO.getName(), response.getBody().get(0).getName());
    }
} 