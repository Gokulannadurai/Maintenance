package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import com.ideas2it.maintenanceservice.entity.Category;
import com.ideas2it.maintenanceservice.repository.CategoryRepository;
import com.ideas2it.maintenanceservice.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock CategoryRepository categoryRepository;
    @InjectMocks CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electrical");
        category.setDescription("Electrical issues");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electrical");
        categoryDTO.setDescription("Electrical issues");
    }

    @Test
    void createCategory_success() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryDTO result = categoryService.createCategory(categoryDTO);
        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_duplicateName_throws() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(categoryDTO));
    }

    @Test
    void updateCategory_success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryDTO result = categoryService.updateCategory(1L, categoryDTO);
        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
    }

    @Test
    void updateCategory_notFound_throws() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(1L, categoryDTO));
    }

    @Test
    void deleteCategory_success() {
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
    }

    @Test
    void deleteCategory_notFound_throws() {
        when(categoryRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void getCategoryById_found() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);
        assertTrue(result.isPresent());
        assertEquals(category.getName(), result.get().getName());
    }

    @Test
    void getCategoryById_notFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<CategoryDTO> result = categoryService.getCategoryById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCategoryByName_found() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        Optional<CategoryDTO> result = categoryService.getCategoryByName("Electrical");
        assertTrue(result.isPresent());
        assertEquals(category.getName(), result.get().getName());
    }

    @Test
    void getCategoryByName_notFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        Optional<CategoryDTO> result = categoryService.getCategoryByName("Electrical");
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCategories_success() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        List<CategoryDTO> result = categoryService.getAllCategories();
        assertEquals(1, result.size());
        assertEquals(category.getName(), result.get(0).getName());
    }
} 