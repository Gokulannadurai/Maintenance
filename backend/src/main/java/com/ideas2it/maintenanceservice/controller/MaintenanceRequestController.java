package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.MaintenanceRequestDTO;
import com.ideas2it.maintenanceservice.service.MaintenanceRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for maintenance request operations.
 */
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j
public class MaintenanceRequestController {

    private final MaintenanceRequestService requestService;

    /**
     * Create a new maintenance request.
     * @param requestDTO the request data
     * @return the created request
     */
    @PostMapping
    public ResponseEntity<MaintenanceRequestDTO> createRequest(@Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        log.info("API: Creating maintenance request: {}", requestDTO.getTitle());
        MaintenanceRequestDTO created = requestService.createRequest(requestDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing maintenance request.
     * @param id the request ID
     * @param requestDTO the updated request data
     * @return the updated request
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRequestDTO> updateRequest(@PathVariable Long id, @Valid @RequestBody MaintenanceRequestDTO requestDTO) {
        log.info("API: Updating maintenance request with id: {}", id);
        MaintenanceRequestDTO updated = requestService.updateRequest(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a maintenance request by ID.
     * @param id the request ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        log.info("API: Deleting maintenance request with id: {}", id);
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a maintenance request by ID.
     * @param id the request ID
     * @return the request if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequestDTO> getRequestById(@PathVariable Long id) {
        log.info("API: Fetching maintenance request by id: {}", id);
        Optional<MaintenanceRequestDTO> request = requestService.getRequestById(id);
        return request.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get maintenance requests by status.
     * @param status the request status
     * @return list of requests
     */
    @GetMapping("/by-status")
    public ResponseEntity<List<MaintenanceRequestDTO>> getRequestsByStatus(@RequestParam String status) {
        log.info("API: Fetching maintenance requests by status: {}", status);
        return ResponseEntity.ok(requestService.getRequestsByStatus(status));
    }

    /**
     * Get maintenance requests assigned to a user.
     * @param userId the assigned user ID
     * @return list of requests
     */
    @GetMapping("/by-assigned")
    public ResponseEntity<List<MaintenanceRequestDTO>> getRequestsByAssignedTo(@RequestParam Long userId) {
        log.info("API: Fetching maintenance requests assigned to user id: {}", userId);
        return ResponseEntity.ok(requestService.getRequestsByAssignedTo(userId));
    }

    /**
     * Get all maintenance requests.
     * @return list of requests
     */
    @GetMapping
    public ResponseEntity<List<MaintenanceRequestDTO>> getAllRequests() {
        log.info("API: Fetching all maintenance requests");
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    /**
     * Assign a maintenance request to a user.
     * @param requestId the request ID
     * @param userId the user ID
     * @return no content
     */
    @PostMapping("/{requestId}/assign")
    public ResponseEntity<Void> assignRequest(@PathVariable Long requestId, @RequestParam Long userId) {
        log.info("API: Assigning request id {} to user id {}", requestId, userId);
        requestService.assignRequest(requestId, userId);
        return ResponseEntity.noContent().build();
    }
} 