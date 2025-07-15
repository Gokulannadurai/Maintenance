package com.ideas2it.maintenanceservice.service;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for request status history operations.
 */
public interface RequestStatusHistoryService {
    RequestStatusHistoryDTO createStatusHistory(RequestStatusHistoryDTO statusHistoryDTO);
    Optional<RequestStatusHistoryDTO> getStatusHistoryById(Long id);
    List<RequestStatusHistoryDTO> getStatusHistoryByRequestId(Long requestId);
} 