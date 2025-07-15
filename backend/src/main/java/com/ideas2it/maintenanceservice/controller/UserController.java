package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import com.ideas2it.maintenanceservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for user-related operations.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     * @param userDTO the user data
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("API: Creating user with email: {}", userDTO.getEmail());
        UserDTO created = userService.createUser(userDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing user.
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("API: Updating user with id: {}", id);
        UserDTO updated = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a user by ID.
     * @param id the user ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("API: Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a user by ID.
     * @param id the user ID
     * @return the user if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("API: Fetching user by id: {}", id);
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get a user by email.
     * @param email the user's email
     * @return the user if found
     */
    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        log.info("API: Fetching user by email: {}", email);
        Optional<UserDTO> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all users.
     * @return list of users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("API: Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Assign a role to a user.
     * @param userId the user ID
     * @param roleName the role name
     * @return no content
     */
    @PostMapping("/{userId}/assign-role")
    public ResponseEntity<Void> assignRole(@PathVariable Long userId, @RequestParam String roleName) {
        log.info("API: Assigning role '{}' to user with id: {}", roleName, userId);
        userService.assignRole(userId, roleName);
        return ResponseEntity.noContent().build();
    }
} 