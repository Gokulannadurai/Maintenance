package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for user-related operations.
 */
public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    void assignRole(Long userId, String roleName);
} 