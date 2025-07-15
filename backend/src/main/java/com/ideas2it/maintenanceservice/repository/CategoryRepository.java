package com.ideas2it.maintenanceservice.repository;

import com.ideas2it.maintenanceservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Find a category by name.
     * @param name the category name
     * @return Optional of Category
     */
    Optional<Category> findByName(String name);
} 