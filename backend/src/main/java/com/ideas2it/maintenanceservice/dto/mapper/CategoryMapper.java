package com.ideas2it.maintenanceservice.dto.mapper;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import com.ideas2it.maintenanceservice.entity.Category;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for Category and CategoryDTO.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
public class CategoryMapper {
    /**
     * Convert Category entity to CategoryDTO.
     */
    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    /**
     * Convert CategoryDTO to Category entity.
     */
    public static Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    /**
     * Convert a list of Category entities to a list of CategoryDTOs.
     */
    public static List<CategoryDTO> toDTOs(List<Category> categories) {
        return categories == null ? null : categories.stream().map(CategoryMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convert a list of CategoryDTOs to a list of Category entities.
     */
    public static List<Category> toEntities(List<CategoryDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(CategoryMapper::toEntity).collect(Collectors.toList());
    }
} 