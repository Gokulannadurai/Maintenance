package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.RequestStatusHistoryDTO;
import com.ideas2it.maintenanceservice.service.RequestStatusHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for request status history operations.
 */
@RestController
@RequestMapping("/api/status-history")
@RequiredArgsConstructor
@Slf4j
public class RequestStatusHistoryController {

    private final RequestStatusHistoryService statusHistoryService;

    /**
     * Create a new status history entry for a maintenance request.
     * @param statusHistoryDTO the status history data
     * @return the created status history entry
     */
    @PostMapping
    public ResponseEntity<RequestStatusHistoryDTO> createStatusHistory(@Valid @RequestBody RequestStatusHistoryDTO statusHistoryDTO) {
        log.info("API: Creating status history for request id: {}", statusHistoryDTO.getRequestId());
        RequestStatusHistoryDTO created = statusHistoryService.createStatusHistory(statusHistoryDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Get a status history entry by ID.
     * @param id the status history ID
     * @return the status history entry if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequestStatusHistoryDTO> getStatusHistoryById(@PathVariable Long id) {
        log.info("API: Fetching status history by id: {}", id);
        Optional<RequestStatusHistoryDTO> statusHistory = statusHistoryService.getStatusHistoryById(id);
        return statusHistory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all status history entries for a maintenance request.
     * @param requestId the maintenance request ID
     * @return list of status history entries
     */
    @GetMapping("/by-request")
    public ResponseEntity<List<RequestStatusHistoryDTO>> getStatusHistoryByRequestId(@RequestParam Long requestId) {
        log.info("API: Fetching status history for request id: {}", requestId);
        return ResponseEntity.ok(statusHistoryService.getStatusHistoryByRequestId(requestId));
    }
} 