package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import com.ideas2it.maintenanceservice.dto.mapper.CategoryMapper;
import com.ideas2it.maintenanceservice.entity.Category;
import com.ideas2it.maintenanceservice.repository.CategoryRepository;
import com.ideas2it.maintenanceservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CategoryService for category-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Create a new category in the system.
     * @param categoryDTO the category data to create
     * @return the created CategoryDTO
     * @throws IllegalArgumentException if category name already exists
     */
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating category with name: {}", categoryDTO.getName());
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = CategoryMapper.toEntity(categoryDTO);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDTO(saved);
    }

    /**
     * Update an existing category's details.
     * @param id the category ID
     * @param categoryDTO the updated category data
     * @return the updated CategoryDTO
     * @throws IllegalArgumentException if category not found
     */
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        log.info("Updating category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toDTO(updated);
    }

    /**
     * Delete a category by ID.
     * @param id the category ID
     * @throws IllegalArgumentException if category not found
     */
    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Get a category by ID.
     * @param id the category ID
     * @return Optional of CategoryDTO if found
     */
    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        log.info("Fetching category by id: {}", id);
        return categoryRepository.findById(id).map(CategoryMapper::toDTO);
    }

    /**
     * Get a category by name.
     * @param name the category name
     * @return Optional of CategoryDTO if found
     */
    @Override
    public Optional<CategoryDTO> getCategoryByName(String name) {
        log.info("Fetching category by name: {}", name);
        return categoryRepository.findByName(name).map(CategoryMapper::toDTO);
    }

    /**
     * Get all categories in the system.
     * @return list of CategoryDTOs
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories");
        return CategoryMapper.toDTOs(categoryRepository.findAll());
    }
} 