package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Role entity.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role by name.
     * @param name the role name
     * @return Optional of Role
     */
    Optional<Role> findByName(String name);
} 