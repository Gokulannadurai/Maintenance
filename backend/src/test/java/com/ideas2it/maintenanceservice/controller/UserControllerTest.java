package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import com.ideas2it.maintenanceservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks UserController userController;
    @Mock UserService userService;
    @Autowired ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFullName("Test User");
        userDTO.setIsActive(true);
        userDTO.setRoles(Set.of("EMPLOYEE"));
    }

    @Test
    void createUser_success() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);
        var response = userController.createUser(userDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void createUser_duplicateEmail_returnsBadRequest() {
        when(userService.createUser(any(UserDTO.class))).thenThrow(new IllegalArgumentException("Email already exists"));
        assertThrows(IllegalArgumentException.class, () -> userController.createUser(userDTO));
    }

    @Test
    void updateUser_success() {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);
        var response = userController.updateUser(1L, userDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void updateUser_userNotFound_returnsBadRequest() {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenThrow(new IllegalArgumentException("User not found"));
        assertThrows(IllegalArgumentException.class, () -> userController.updateUser(1L, userDTO));
    }

    @Test
    void deleteUser_success() {
        doNothing().when(userService).deleteUser(1L);
        var response = userController.deleteUser(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_userNotFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("User not found")).when(userService).deleteUser(1L);
        assertThrows(IllegalArgumentException.class, () -> userController.deleteUser(1L));
    }

    @Test
    void getUserById_found() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));
        var response = userController.getUserById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void getUserById_notFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());
        var response = userController.getUserById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getUserByEmail_found() {
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(userDTO));
        var response = userController.getUserByEmail("test@example.com");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void getUserByEmail_notFound() {
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());
        var response = userController.getUserByEmail("test@example.com");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllUsers_success() {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));
        var response = userController.getAllUsers();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(userDTO.getEmail(), response.getBody().get(0).getEmail());
    }

    @Test
    void assignRole_success() {
        doNothing().when(userService).assignRole(1L, "EMPLOYEE");
        var response = userController.assignRole(1L, "EMPLOYEE");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void assignRole_userOrRoleNotFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("User or role not found")).when(userService).assignRole(1L, "EMPLOYEE");
        assertThrows(IllegalArgumentException.class, () -> userController.assignRole(1L, "EMPLOYEE"));
    }
} 