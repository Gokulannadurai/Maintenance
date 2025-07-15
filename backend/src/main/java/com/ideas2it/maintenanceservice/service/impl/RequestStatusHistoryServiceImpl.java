package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import com.ideas2it.maintenanceservice.dto.mapper.RequestStatusHistoryMapper;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.RequestStatusHistory;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.RequestStatusHistoryRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.RequestStatusHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of RequestStatusHistoryService for request status history operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RequestStatusHistoryServiceImpl implements RequestStatusHistoryService {

    private final RequestStatusHistoryRepository statusHistoryRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final UserRepository userRepository;

    /**
     * Create a new status history entry for a maintenance request.
     * @param statusHistoryDTO the status history data to create
     * @return the created RequestStatusHistoryDTO
     * @throws IllegalArgumentException if referenced entities not found
     */
    @Override
    public RequestStatusHistoryDTO createStatusHistory(RequestStatusHistoryDTO statusHistoryDTO) {
        log.info("Creating status history for request id: {}", statusHistoryDTO.getRequestId());
        MaintenanceRequest request = requestRepository.findById(statusHistoryDTO.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        User changedBy = userRepository.findById(statusHistoryDTO.getChangedById())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        RequestStatusHistory statusHistory = RequestStatusHistoryMapper.toEntity(statusHistoryDTO);
        statusHistory.setRequest(request);
        statusHistory.setChangedBy(changedBy);
        RequestStatusHistory saved = statusHistoryRepository.save(statusHistory);
        return RequestStatusHistoryMapper.toDTO(saved);
    }

    /**
     * Get a status history entry by ID.
     * @param id the status history ID
     * @return Optional of RequestStatusHistoryDTO if found
     */
    @Override
    public Optional<RequestStatusHistoryDTO> getStatusHistoryById(Long id) {
        log.info("Fetching status history by id: {}", id);
        return statusHistoryRepository.findById(id).map(RequestStatusHistoryMapper::toDTO);
    }

    /**
     * Get all status history entries for a maintenance request.
     * @param requestId the maintenance request ID
     * @return list of RequestStatusHistoryDTOs
     */
    @Override
    public List<RequestStatusHistoryDTO> getStatusHistoryByRequestId(Long requestId) {
        log.info("Fetching status history for request id: {}", requestId);
        return RequestStatusHistoryMapper.toDTOs(statusHistoryRepository.findByRequestId(requestId));
    }
} 