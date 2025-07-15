package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import com.ideas2it.maintenanceservice.service.RequestStatusHistoryService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestStatusHistoryControllerTest {
    @InjectMocks RequestStatusHistoryController requestStatusHistoryController;
    @Mock RequestStatusHistoryService statusHistoryService;
    private RequestStatusHistoryDTO statusHistoryDTO;

    @BeforeEach
    void setUp() {
        statusHistoryDTO = new RequestStatusHistoryDTO();
        statusHistoryDTO.setId(1L);
        statusHistoryDTO.setRequestId(1L);
        statusHistoryDTO.setOldStatus("OPEN");
        statusHistoryDTO.setNewStatus("IN_PROGRESS");
        statusHistoryDTO.setChangedById(1L);
        statusHistoryDTO.setChangedAt(LocalDateTime.now());
    }

    @Test
    void createStatusHistory_success() {
        when(statusHistoryService.createStatusHistory(any(RequestStatusHistoryDTO.class))).thenReturn(statusHistoryDTO);
        var response = requestStatusHistoryController.createStatusHistory(statusHistoryDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(statusHistoryDTO.getNewStatus(), response.getBody().getNewStatus());
    }

    @Test
    void createStatusHistory_requestNotFound_returnsBadRequest() {
        when(statusHistoryService.createStatusHistory(any(RequestStatusHistoryDTO.class))).thenThrow(new IllegalArgumentException("Request not found"));
        assertThrows(IllegalArgumentException.class, () -> requestStatusHistoryController.createStatusHistory(statusHistoryDTO));
    }

    @Test
    void getStatusHistoryById_found() {
        when(statusHistoryService.getStatusHistoryById(1L)).thenReturn(Optional.of(statusHistoryDTO));
        var response = requestStatusHistoryController.getStatusHistoryById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(statusHistoryDTO.getNewStatus(), response.getBody().getNewStatus());
    }

    @Test
    void getStatusHistoryById_notFound() {
        when(statusHistoryService.getStatusHistoryById(1L)).thenReturn(Optional.empty());
        var response = requestStatusHistoryController.getStatusHistoryById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getStatusHistoryByRequestId_success() {
        when(statusHistoryService.getStatusHistoryByRequestId(1L)).thenReturn(List.of(statusHistoryDTO));
        var response = requestStatusHistoryController.getStatusHistoryByRequestId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(statusHistoryDTO.getNewStatus(), response.getBody().get(0).getNewStatus());
    }
} 