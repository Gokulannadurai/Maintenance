package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for maintenance request operations.
 */
public interface MaintenanceRequestService {
    MaintenanceRequestDTO createRequest(MaintenanceRequestDTO requestDTO);
    MaintenanceRequestDTO updateRequest(Long id, MaintenanceRequestDTO requestDTO);
    void deleteRequest(Long id);
    Optional<MaintenanceRequestDTO> getRequestById(Long id);
    List<MaintenanceRequestDTO> getRequestsByStatus(String status);
    List<MaintenanceRequestDTO> getRequestsByAssignedTo(Long userId);
    List<MaintenanceRequestDTO> getAllRequests();
    void assignRequest(Long requestId, Long userId);
} 