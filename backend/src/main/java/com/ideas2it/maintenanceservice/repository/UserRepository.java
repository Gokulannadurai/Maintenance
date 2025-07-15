package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by email.
     * @param email the user's email
     * @return Optional of User
     */
    Optional<User> findByEmail(String email);
} 