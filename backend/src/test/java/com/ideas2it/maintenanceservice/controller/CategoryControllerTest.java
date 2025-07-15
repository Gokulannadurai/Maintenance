package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.CategoryDTO;
import com.ideas2it.maintenanceservice.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @InjectMocks CategoryController categoryController;
    @Mock CategoryService categoryService;
    @Autowired ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electrical");
        categoryDTO.setDescription("Electrical issues");
    }

    @Test
    void createCategory_success() {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);
        var response = categoryController.createCategory(categoryDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryDTO.getName(), response.getBody().getName());
    }

    @Test
    void createCategory_duplicateName_returnsBadRequest() {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenThrow(new IllegalArgumentException("Category name already exists"));
        assertThrows(IllegalArgumentException.class, () -> categoryController.createCategory(categoryDTO));
    }

    @Test
    void updateCategory_success() {
        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenReturn(categoryDTO);
        var response = categoryController.updateCategory(1L, categoryDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryDTO.getName(), response.getBody().getName());
    }

    @Test
    void updateCategory_notFound_returnsBadRequest() {
        when(categoryService.updateCategory(eq(1L), any(CategoryDTO.class))).thenThrow(new IllegalArgumentException("Category not found"));
        assertThrows(IllegalArgumentException.class, () -> categoryController.updateCategory(1L, categoryDTO));
    }

    @Test
    void deleteCategory_success() {
        doNothing().when(categoryService).deleteCategory(1L);
        var response = categoryController.deleteCategory(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteCategory_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Category not found")).when(categoryService).deleteCategory(1L);
        assertThrows(IllegalArgumentException.class, () -> categoryController.deleteCategory(1L));
    }

    @Test
    void getCategoryById_found() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(categoryDTO));
        var response = categoryController.getCategoryById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryDTO.getName(), response.getBody().getName());
    }

    @Test
    void getCategoryById_notFound() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());
        var response = categoryController.getCategoryById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getCategoryByName_found() {
        when(categoryService.getCategoryByName(anyString())).thenReturn(Optional.of(categoryDTO));
        var response = categoryController.getCategoryByName("Electrical");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categoryDTO.getName(), response.getBody().getName());
    }

    @Test
    void getCategoryByName_notFound() {
        when(categoryService.getCategoryByName(anyString())).thenReturn(Optional.empty());
        var response = categoryController.getCategoryByName("Electrical");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllCategories_success() {
        when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO));
        var response = categoryController.getAllCategories();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(categoryDTO.getName(), response.getBody().get(0).getName());
    }
} 