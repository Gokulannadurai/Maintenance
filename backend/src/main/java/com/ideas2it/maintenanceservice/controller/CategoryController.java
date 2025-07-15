package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import com.ideas2it.maintenanceservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for category-related operations.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create a new category.
     * @param categoryDTO the category data
     * @return the created category
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("API: Creating category with name: {}", categoryDTO.getName());
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing category.
     * @param id the category ID
     * @param categoryDTO the updated category data
     * @return the updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("API: Updating category with id: {}", id);
        CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a category by ID.
     * @param id the category ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("API: Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a category by ID.
     * @param id the category ID
     * @return the category if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        log.info("API: Fetching category by id: {}", id);
        Optional<CategoryDTO> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get a category by name.
     * @param name the category name
     * @return the category if found
     */
    @GetMapping("/by-name")
    public ResponseEntity<CategoryDTO> getCategoryByName(@RequestParam String name) {
        log.info("API: Fetching category by name: {}", name);
        Optional<CategoryDTO> category = categoryService.getCategoryByName(name);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all categories.
     * @return list of categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.info("API: Fetching all categories");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
} 