package com.ideas2it.maintenanceservice.dto;

import lombok.Data;
import java.util.Set;

/**
 * Data Transfer Object for User entity.
 */
@Data
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Set<String> roles;
} 