package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.RequestStatusHistory;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.RequestStatusHistoryRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.RequestStatusHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestStatusHistoryServiceTest {
    @Mock RequestStatusHistoryRepository statusHistoryRepository;
    @Mock MaintenanceRequestRepository requestRepository;
    @Mock UserRepository userRepository;
    @InjectMocks RequestStatusHistoryServiceImpl statusHistoryService;

    private RequestStatusHistory statusHistory;
    private RequestStatusHistoryDTO statusHistoryDTO;
    private MaintenanceRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        request = new MaintenanceRequest();
        request.setId(1L);
        statusHistory = new RequestStatusHistory();
        statusHistory.setId(1L);
        statusHistory.setRequest(request);
        statusHistory.setChangedBy(user);
        statusHistory.setOldStatus(RequestStatusHistory.Status.OPEN);
        statusHistory.setNewStatus(RequestStatusHistory.Status.IN_PROGRESS);
        statusHistory.setChangedAt(LocalDateTime.now());
        statusHistoryDTO = new RequestStatusHistoryDTO();
        statusHistoryDTO.setId(1L);
        statusHistoryDTO.setRequestId(1L);
        statusHistoryDTO.setChangedById(1L);
        statusHistoryDTO.setOldStatus("OPEN");
        statusHistoryDTO.setNewStatus("IN_PROGRESS");
        statusHistoryDTO.setChangedAt(statusHistory.getChangedAt());
    }

    @Test
    void createStatusHistory_success() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(statusHistoryRepository.save(any(RequestStatusHistory.class))).thenReturn(statusHistory);
        RequestStatusHistoryDTO result = statusHistoryService.createStatusHistory(statusHistoryDTO);
        assertNotNull(result);
        assertEquals(statusHistory.getNewStatus().toString(), result.getNewStatus());
    }

    @Test
    void createStatusHistory_requestNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> statusHistoryService.createStatusHistory(statusHistoryDTO));
    }

    @Test
    void createStatusHistory_userNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> statusHistoryService.createStatusHistory(statusHistoryDTO));
    }

    @Test
    void getStatusHistoryById_found() {
        when(statusHistoryRepository.findById(anyLong())).thenReturn(Optional.of(statusHistory));
        Optional<RequestStatusHistoryDTO> result = statusHistoryService.getStatusHistoryById(1L);
        assertTrue(result.isPresent());
        assertEquals(statusHistory.getNewStatus().toString(), result.get().getNewStatus());
    }

    @Test
    void getStatusHistoryById_notFound() {
        when(statusHistoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<RequestStatusHistoryDTO> result = statusHistoryService.getStatusHistoryById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getStatusHistoryByRequestId_success() {
        when(statusHistoryRepository.findByRequestId(anyLong())).thenReturn(List.of(statusHistory));
        List<RequestStatusHistoryDTO> result = statusHistoryService.getStatusHistoryByRequestId(1L);
        assertEquals(1, result.size());
        assertEquals(statusHistory.getNewStatus().toString(), result.get(0).getNewStatus());
    }
} 