package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.AuditLogDTO;
import com.ideas2it.maintenanceservice.dto.mapper.AuditLogMapper;
import com.ideas2it.maintenanceservice.entity.AuditLog;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.AuditLogRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of AuditLogService for audit log operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    /**
     * Create a new audit log entry for a user action.
     * @param auditLogDTO the audit log data to create
     * @return the created AuditLogDTO
     * @throws IllegalArgumentException if user not found
     */
    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        log.info("Creating audit log for user id: {}", auditLogDTO.getUserId());
        User user = userRepository.findById(auditLogDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        AuditLog auditLog = AuditLogMapper.toEntity(auditLogDTO);
        auditLog.setUser(user);
        AuditLog saved = auditLogRepository.save(auditLog);
        return AuditLogMapper.toDTO(saved);
    }

    /**
     * Get all audit logs for a user.
     * @param userId the user ID
     * @return list of AuditLogDTOs
     */
    @Override
    public List<AuditLogDTO> getAuditLogsByUserId(Long userId) {
        log.info("Fetching audit logs for user id: {}", userId);
        return AuditLogMapper.toDTOs(auditLogRepository.findByUserId(userId));
    }
} 