package com.ideas2it.maintenanceservice.service.impl;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import com.ideas2it.maintenanceservice.dto.mapper.AttachmentMapper;
import com.ideas2it.maintenanceservice.entity.Attachment;
import com.ideas2it.maintenanceservice.entity.MaintenanceRequest;
import com.ideas2it.maintenanceservice.entity.User;
import com.ideas2it.maintenanceservice.repository.AttachmentRepository;
import com.ideas2it.maintenanceservice.repository.MaintenanceRequestRepository;
import com.ideas2it.maintenanceservice.repository.UserRepository;
import com.ideas2it.maintenanceservice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of AttachmentService for attachment operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final MaintenanceRequestRepository requestRepository;

    /**
     * Create a new attachment for a maintenance request.
     * @param attachmentDTO the attachment data to create
     * @return the created AttachmentDTO
     * @throws IllegalArgumentException if referenced entities not found
     */
    @Override
    public AttachmentDTO createAttachment(AttachmentDTO attachmentDTO) {
        log.info("Creating attachment for request id: {}", attachmentDTO.getRequestId());
        MaintenanceRequest request = requestRepository.findById(attachmentDTO.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        User uploadedBy = userRepository.findById(attachmentDTO.getUploadedById())
                .orElseThrow(() -> new IllegalArgumentException("Uploader not found"));
        Attachment attachment = AttachmentMapper.toEntity(attachmentDTO);
        attachment.setRequest(request);
        attachment.setUploadedBy(uploadedBy);
        Attachment saved = attachmentRepository.save(attachment);
        return AttachmentMapper.toDTO(saved);
    }

    /**
     * Delete an attachment by ID.
     * @param id the attachment ID
     * @throws IllegalArgumentException if attachment not found
     */
    @Override
    public void deleteAttachment(Long id) {
        log.info("Deleting attachment with id: {}", id);
        if (!attachmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Attachment not found");
        }
        attachmentRepository.deleteById(id);
    }

    /**
     * Get an attachment by ID.
     * @param id the attachment ID
     * @return Optional of AttachmentDTO if found
     */
    @Override
    public Optional<AttachmentDTO> getAttachmentById(Long id) {
        log.info("Fetching attachment by id: {}", id);
        return attachmentRepository.findById(id).map(AttachmentMapper::toDTO);
    }

    /**
     * Get all attachments for a maintenance request.
     * @param requestId the maintenance request ID
     * @return list of AttachmentDTOs
     */
    @Override
    public List<AttachmentDTO> getAttachmentsByRequestId(Long requestId) {
        log.info("Fetching attachments for request id: {}", requestId);
        return AttachmentMapper.toDTOs(attachmentRepository.findByRequestId(requestId));
    }
} 