package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import com.ideas2it.maintenanceservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for audit log operations.
 */
@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@Slf4j
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Create a new audit log entry for a user action.
     * @param auditLogDTO the audit log data
     * @return the created audit log entry
     */
    @PostMapping
    public ResponseEntity<AuditLogDTO> createAuditLog(@Valid @RequestBody AuditLogDTO auditLogDTO) {
        log.info("API: Creating audit log for user id: {}", auditLogDTO.getUserId());
        AuditLogDTO created = auditLogService.createAuditLog(auditLogDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Get all audit logs for a user.
     * @param userId the user ID
     * @return list of audit logs
     */
    @GetMapping("/by-user")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUserId(@RequestParam Long userId) {
        log.info("API: Fetching audit logs for user id: {}", userId);
        return ResponseEntity.ok(auditLogService.getAuditLogsByUserId(userId));
    }
} 