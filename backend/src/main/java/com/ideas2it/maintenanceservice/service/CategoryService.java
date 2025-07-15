package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for category-related operations.
 */
public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    Optional<CategoryDTO> getCategoryById(Long id);
    Optional<CategoryDTO> getCategoryByName(String name);
    List<CategoryDTO> getAllCategories();
} 