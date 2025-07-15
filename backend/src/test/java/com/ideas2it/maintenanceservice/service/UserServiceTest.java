package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import com.ideas2it.maintenanceservice.entity.Role;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.RoleRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.UserServiceImpl;
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
class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @InjectMocks UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setIsActive(true);
        user.setRoles(new HashSet<>());

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFullName("Test User");
        userDTO.setIsActive(true);
        userDTO.setRoles(Set.of("EMPLOYEE"));

        role = new Role();
        role.setId(1L);
        role.setName("EMPLOYEE");
    }

    @Test
    void createUser_success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        UserDTO result = userService.createUser(userDTO);
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail_throws() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void updateUser_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO result = userService.updateUser(1L, userDTO);
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, userDTO));
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_userNotFound_throws() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Optional<UserDTO> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<UserDTO> result = userService.getUserById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserByEmail_found() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Optional<UserDTO> result = userService.getUserByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void getUserByEmail_notFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        Optional<UserDTO> result = userService.getUserByEmail("test@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUsers_success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    void assignRole_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userService.assignRole(1L, "EMPLOYEE"));
    }

    @Test
    void assignRole_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.assignRole(1L, "EMPLOYEE"));
    }

    @Test
    void assignRole_roleNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.assignRole(1L, "EMPLOYEE"));
    }
} 