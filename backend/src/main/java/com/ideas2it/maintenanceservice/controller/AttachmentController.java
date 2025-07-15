package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.dto.AttachmentDTO;
import com.ideas2it.maintenanceservice.service.AttachmentService;
import com.ideas2it.maintenanceservice.service.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for attachment operations.
 */
@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final S3FileService s3FileService;

    /**
     * Create a new attachment for a maintenance request.
     * @param attachmentDTO the attachment data
     * @return the created attachment
     */
    @PostMapping
    public ResponseEntity<AttachmentDTO> createAttachment(@Valid @RequestBody AttachmentDTO attachmentDTO) {
        log.info("API: Creating attachment for request id: {}", attachmentDTO.getRequestId());
        AttachmentDTO created = attachmentService.createAttachment(attachmentDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * Delete an attachment by ID.
     * @param id the attachment ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.info("API: Deleting attachment with id: {}", id);
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get an attachment by ID.
     * @param id the attachment ID
     * @return the attachment if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttachmentDTO> getAttachmentById(@PathVariable Long id) {
        log.info("API: Fetching attachment by id: {}", id);
        Optional<AttachmentDTO> attachment = attachmentService.getAttachmentById(id);
        return attachment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all attachments for a maintenance request.
     * @param requestId the maintenance request ID
     * @return list of attachments
     */
    @GetMapping("/by-request")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByRequestId(@RequestParam Long requestId) {
        log.info("API: Fetching attachments for request id: {}", requestId);
        return ResponseEntity.ok(attachmentService.getAttachmentsByRequestId(requestId));
    }

    /**
     * Upload a file to S3 and create attachment metadata.
     * @param file the file to upload
     * @param requestId the maintenance request ID
     * @return the created attachment
     */
    @PostMapping("/upload")
    public ResponseEntity<AttachmentDTO> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("requestId") Long requestId) {
        log.info("API: Uploading attachment for request id: {}", requestId);
        if (file.isEmpty()) {
            log.warn("Upload failed: file is empty");
            return ResponseEntity.badRequest().build();
        }
        try {
            String s3Key = s3FileService.uploadFile(file);
            AttachmentDTO attachmentDTO = new AttachmentDTO();
            attachmentDTO.setRequestId(requestId);
            attachmentDTO.setFileName(file.getOriginalFilename());
            attachmentDTO.setS3key(s3Key);
            attachmentDTO.setFileType(file.getContentType());
            AttachmentDTO created = attachmentService.createAttachment(attachmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            log.error("Failed to upload attachment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Download a file from S3 by attachment ID.
     * @param id the attachment ID
     * @return the file as a byte array
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        log.info("API: Downloading attachment with id: {}", id);
        Optional<AttachmentDTO> attachmentOpt = attachmentService.getAttachmentById(id);
        if (attachmentOpt.isEmpty()) {
            log.warn("Download failed: attachment not found for id {}", id);
            return ResponseEntity.notFound().build();
        }
        AttachmentDTO attachment = attachmentOpt.get();
        try {
            byte[] fileBytes = s3FileService.downloadFile(attachment.getS3key());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .body(fileBytes);
        } catch (Exception e) {
            log.error("Failed to download attachment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 