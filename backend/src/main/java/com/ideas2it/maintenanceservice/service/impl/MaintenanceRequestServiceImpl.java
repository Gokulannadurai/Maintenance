package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import com.ideas2it.maintenanceservice.dto.mapper.MaintenanceRequestMapper;
import com.ideas2it.maintenanceservice.entity.Category;
import com.ideas2it.maintenanceservice.entity.Location;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.CategoryRepository;
import com.ideas2it.maintenanceservice.repository.LocationRepository;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.MaintenanceRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of MaintenanceRequestService for maintenance request operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MaintenanceRequestServiceImpl implements MaintenanceRequestService {

    private final MaintenanceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    /**
     * Create a new maintenance request.
     * @param requestDTO the request data to create
     * @return the created MaintenanceRequestDTO
     * @throws IllegalArgumentException if referenced entities not found
     */
    @Override
    public MaintenanceRequestDTO createRequest(MaintenanceRequestDTO requestDTO) {
        log.info("Creating maintenance request: {}", requestDTO.getTitle());
        User requester = userRepository.findById(requestDTO.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Location location = locationRepository.findById(requestDTO.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        MaintenanceRequest request = MaintenanceRequestMapper.toEntity(requestDTO);
        request.setRequester(requester);
        request.setCategory(category);
        request.setLocation(location);
        if (requestDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(requestDTO.getAssignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
            request.setAssignedTo(assignedTo);
        }
        MaintenanceRequest saved = requestRepository.save(request);
        return MaintenanceRequestMapper.toDTO(saved);
    }

    /**
     * Update an existing maintenance request.
     * @param id the request ID
     * @param requestDTO the updated request data
     * @return the updated MaintenanceRequestDTO
     * @throws IllegalArgumentException if request or referenced entities not found
     */
    @Override
    public MaintenanceRequestDTO updateRequest(Long id, MaintenanceRequestDTO requestDTO) {
        log.info("Updating maintenance request with id: {}", id);
        MaintenanceRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        if (requestDTO.getTitle() != null) request.setTitle(requestDTO.getTitle());
        if (requestDTO.getDescription() != null) request.setDescription(requestDTO.getDescription());
        if (requestDTO.getStatus() != null) request.setStatus(MaintenanceRequest.Status.valueOf(requestDTO.getStatus()));
        if (requestDTO.getPriority() != null) request.setPriority(MaintenanceRequest.Priority.valueOf(requestDTO.getPriority()));
        if (requestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            request.setCategory(category);
        }
        if (requestDTO.getLocationId() != null) {
            Location location = locationRepository.findById(requestDTO.getLocationId())
                    .orElseThrow(() -> new IllegalArgumentException("Location not found"));
            request.setLocation(location);
        }
        if (requestDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(requestDTO.getAssignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
            request.setAssignedTo(assignedTo);
        }
        MaintenanceRequest updated = requestRepository.save(request);
        return MaintenanceRequestMapper.toDTO(updated);
    }

    /**
     * Delete a maintenance request by ID.
     * @param id the request ID
     * @throws IllegalArgumentException if request not found
     */
    @Override
    public void deleteRequest(Long id) {
        log.info("Deleting maintenance request with id: {}", id);
        if (!requestRepository.existsById(id)) {
            throw new IllegalArgumentException("Request not found");
        }
        requestRepository.deleteById(id);
    }

    /**
     * Get a maintenance request by ID.
     * @param id the request ID
     * @return Optional of MaintenanceRequestDTO if found
     */
    @Override
    public Optional<MaintenanceRequestDTO> getRequestById(Long id) {
        log.info("Fetching maintenance request by id: {}", id);
        return requestRepository.findById(id).map(MaintenanceRequestMapper::toDTO);
    }

    /**
     * Get maintenance requests by status.
     * @param status the request status
     * @return list of MaintenanceRequestDTOs
     */
    @Override
    public List<MaintenanceRequestDTO> getRequestsByStatus(String status) {
        log.info("Fetching maintenance requests by status: {}", status);
        return MaintenanceRequestMapper.toDTOs(requestRepository.findByStatus(MaintenanceRequest.Status.valueOf(status)));
    }

    /**
     * Get maintenance requests assigned to a user.
     * @param userId the assigned user ID
     * @return list of MaintenanceRequestDTOs
     */
    @Override
    public List<MaintenanceRequestDTO> getRequestsByAssignedTo(Long userId) {
        log.info("Fetching maintenance requests assigned to user id: {}", userId);
        User assignedTo = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Assigned user not found"));
        return MaintenanceRequestMapper.toDTOs(requestRepository.findByAssignedTo(assignedTo));
    }

    /**
     * Get all maintenance requests in the system.
     * @return list of MaintenanceRequestDTOs
     */
    @Override
    public List<MaintenanceRequestDTO> getAllRequests() {
        log.info("Fetching all maintenance requests");
        return MaintenanceRequestMapper.toDTOs(requestRepository.findAll());
    }

    /**
     * Assign a maintenance request to a user.
     * @param requestId the request ID
     * @param userId the user ID
     * @throws IllegalArgumentException if request or user not found
     */
    @Override
    public void assignRequest(Long requestId, Long userId) {
        log.info("Assigning request id {} to user id {}", requestId, userId);
        MaintenanceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        request.setAssignedTo(user);
        requestRepository.save(request);
    }
} 