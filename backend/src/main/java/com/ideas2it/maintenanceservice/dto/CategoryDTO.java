package com.ideas2it.maintenanceservice.dto;

import lombok.Data;

/**
 * Data Transfer Object for Category entity.
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
} 