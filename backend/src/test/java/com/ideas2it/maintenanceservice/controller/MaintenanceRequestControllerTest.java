package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import com.ideas2it.maintenanceservice.service.MaintenanceRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.ideas2it.maintenanceservice.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceRequestControllerTest {
    @InjectMocks MaintenanceRequestController maintenanceRequestController;
    @Mock MaintenanceRequestService requestService;
    private MaintenanceRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new MaintenanceRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setTitle("Fix AC");
        requestDTO.setStatus("OPEN");
        requestDTO.setPriority("NORMAL");
        requestDTO.setRequesterId(1L);
        requestDTO.setCategoryId(1L);
        requestDTO.setLocationId(1L);
    }

    @Test
    void createRequest_success() {
        when(requestService.createRequest(any(MaintenanceRequestDTO.class))).thenReturn(requestDTO);
        var response = maintenanceRequestController.createRequest(requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requestDTO.getTitle(), response.getBody().getTitle());
    }

    @Test
    void updateRequest_success() {
        when(requestService.updateRequest(eq(1L), any(MaintenanceRequestDTO.class))).thenReturn(requestDTO);
        var response = maintenanceRequestController.updateRequest(1L, requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requestDTO.getTitle(), response.getBody().getTitle());
    }

    @Test
    void updateRequest_notFound_returnsBadRequest() {
        when(requestService.updateRequest(eq(1L), any(MaintenanceRequestDTO.class))).thenThrow(new IllegalArgumentException("Request not found"));
        assertThrows(IllegalArgumentException.class, () -> maintenanceRequestController.updateRequest(1L, requestDTO));
    }

    @Test
    void deleteRequest_success() {
        doNothing().when(requestService).deleteRequest(1L);
        var response = maintenanceRequestController.deleteRequest(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteRequest_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Request not found")).when(requestService).deleteRequest(1L);
        assertThrows(IllegalArgumentException.class, () -> maintenanceRequestController.deleteRequest(1L));
    }

    @Test
    void getRequestById_found() {
        when(requestService.getRequestById(1L)).thenReturn(Optional.of(requestDTO));
        var response = maintenanceRequestController.getRequestById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requestDTO.getTitle(), response.getBody().getTitle());
    }

    @Test
    void getRequestById_notFound() {
        when(requestService.getRequestById(1L)).thenReturn(Optional.empty());
        var response = maintenanceRequestController.getRequestById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getRequestsByStatus_success() {
        when(requestService.getRequestsByStatus(anyString())).thenReturn(List.of(requestDTO));
        var response = maintenanceRequestController.getRequestsByStatus("OPEN");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(requestDTO.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void getRequestsByAssignedTo_success() {
        when(requestService.getRequestsByAssignedTo(anyLong())).thenReturn(List.of(requestDTO));
        var response = maintenanceRequestController.getRequestsByAssignedTo(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(requestDTO.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void getAllRequests_success() {
        when(requestService.getAllRequests()).thenReturn(List.of(requestDTO));
        var response = maintenanceRequestController.getAllRequests();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(requestDTO.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void assignRequest_success() {
        doNothing().when(requestService).assignRequest(1L, 1L);
        var response = maintenanceRequestController.assignRequest(1L, 1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void assignRequest_notFound_returnsBadRequest() {
        doThrow(new IllegalArgumentException("Request or user not found")).when(requestService).assignRequest(1L, 1L);
        assertThrows(IllegalArgumentException.class, () -> maintenanceRequestController.assignRequest(1L, 1L));
    }
} 