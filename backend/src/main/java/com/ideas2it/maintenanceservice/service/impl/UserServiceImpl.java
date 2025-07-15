package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.UserDTO;
import com.ideas2it.maintenanceservice.dto.mapper.UserMapper;
import com.ideas2it.maintenanceservice.entity.Role;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.RoleRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of UserService for user-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Create a new user in the system.
     * @param userDTO the user data to create
     * @return the created UserDTO
     * @throws IllegalArgumentException if email already exists
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user with email: {}", userDTO.getEmail());
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = UserMapper.toEntity(userDTO, roleRepository);
        user.setRoles(new HashSet<>());
        user.setIsActive(true);
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    /**
     * Update an existing user's details.
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated UserDTO
     * @throws IllegalArgumentException if user not found
     */
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setFullName(userDTO.getFullName());
        user.setIsActive(userDTO.getIsActive());
        User updated = userRepository.save(user);
        return UserMapper.toDTO(updated);
    }

    /**
     * Delete a user by ID.
     * @param id the user ID
     * @throws IllegalArgumentException if user not found
     */
    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Get a user by ID.
     * @param id the user ID
     * @return Optional of UserDTO if found
     */
    @Override
    public Optional<UserDTO> getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    /**
     * Get a user by email.
     * @param email the user's email
     * @return Optional of UserDTO if found
     */
    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email).map(UserMapper::toDTO);
    }

    /**
     * Get all users in the system.
     * @return list of UserDTOs
     */
    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return UserMapper.toDTOs(userRepository.findAll());
    }

    /**
     * Assign a role to a user.
     * @param userId the user ID
     * @param roleName the role name
     * @throws IllegalArgumentException if user or role not found
     */
    @Override
    public void assignRole(Long userId, String roleName) {
        log.info("Assigning role '{}' to user with id: {}", roleName, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
} 