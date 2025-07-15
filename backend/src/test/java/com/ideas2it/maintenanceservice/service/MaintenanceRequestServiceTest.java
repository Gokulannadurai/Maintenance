package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import com.ideas2it.maintenanceservice.entity.Category;
import com.ideas2it.maintenanceservice.entity.Location;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.CategoryRepository;
import com.ideas2it.maintenanceservice.repository.LocationRepository;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.impl.MaintenanceRequestServiceImpl;
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
class MaintenanceRequestServiceTest {
    @Mock MaintenanceRequestRepository requestRepository;
    @Mock UserRepository userRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock LocationRepository locationRepository;
    @InjectMocks MaintenanceRequestServiceImpl requestService;

    private MaintenanceRequest request;
    private MaintenanceRequestDTO requestDTO;
    private User user;
    private Category category;
    private Location location;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("Test User");
        category = new Category();
        category.setId(1L);
        category.setName("Electrical");
        location = new Location();
        location.setId(1L);
        location.setName("Conference Room");
        request = new MaintenanceRequest();
        request.setId(1L);
        request.setTitle("Fix AC");
        request.setRequester(user);
        request.setCategory(category);
        request.setLocation(location);
        request.setStatus(MaintenanceRequest.Status.OPEN);
        request.setPriority(MaintenanceRequest.Priority.NORMAL);
        requestDTO = new MaintenanceRequestDTO();
        requestDTO.setId(1L);
        requestDTO.setTitle("Fix AC");
        requestDTO.setRequesterId(1L);
        requestDTO.setCategoryId(1L);
        requestDTO.setLocationId(1L);
        requestDTO.setStatus("OPEN");
        requestDTO.setPriority("NORMAL");
    }

    @Test
    void createRequest_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(requestRepository.save(any(MaintenanceRequest.class))).thenReturn(request);
        MaintenanceRequestDTO result = requestService.createRequest(requestDTO);
        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
    }

    @Test
    void createRequest_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.createRequest(requestDTO));
    }

    @Test
    void createRequest_categoryNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.createRequest(requestDTO));
    }

    @Test
    void createRequest_locationNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.createRequest(requestDTO));
    }

    @Test
    void updateRequest_success() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(requestRepository.save(any(MaintenanceRequest.class))).thenReturn(request);
        MaintenanceRequestDTO result = requestService.updateRequest(1L, requestDTO);
        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
    }

    @Test
    void updateRequest_notFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.updateRequest(1L, requestDTO));
    }

    @Test
    void deleteRequest_success() {
        when(requestRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(requestRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> requestService.deleteRequest(1L));
    }

    @Test
    void deleteRequest_notFound_throws() {
        when(requestRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> requestService.deleteRequest(1L));
    }

    @Test
    void getRequestById_found() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        Optional<MaintenanceRequestDTO> result = requestService.getRequestById(1L);
        assertTrue(result.isPresent());
        assertEquals(request.getTitle(), result.get().getTitle());
    }

    @Test
    void getRequestById_notFound() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<MaintenanceRequestDTO> result = requestService.getRequestById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRequestsByStatus_success() {
        when(requestRepository.findByStatus(any())).thenReturn(List.of(request));
        List<MaintenanceRequestDTO> result = requestService.getRequestsByStatus("OPEN");
        assertEquals(1, result.size());
        assertEquals(request.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getRequestsByAssignedTo_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findByAssignedTo(any(User.class))).thenReturn(List.of(request));
        List<MaintenanceRequestDTO> result = requestService.getRequestsByAssignedTo(1L);
        assertEquals(1, result.size());
        assertEquals(request.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getRequestsByAssignedTo_userNotFound_throws() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.getRequestsByAssignedTo(1L));
    }

    @Test
    void getAllRequests_success() {
        when(requestRepository.findAll()).thenReturn(List.of(request));
        List<MaintenanceRequestDTO> result = requestService.getAllRequests();
        assertEquals(1, result.size());
        assertEquals(request.getTitle(), result.get(0).getTitle());
    }

    @Test
    void assignRequest_success() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(MaintenanceRequest.class))).thenReturn(request);
        assertDoesNotThrow(() -> requestService.assignRequest(1L, 1L));
    }

    @Test
    void assignRequest_requestNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.assignRequest(1L, 1L));
    }

    @Test
    void assignRequest_userNotFound_throws() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> requestService.assignRequest(1L, 1L));
    }
} 