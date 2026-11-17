package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.AiJobStatusDTO;
import com.platform.billprocessing.exception.RateLimitExceededException;
import com.platform.billprocessing.service.AiBillExtractionService;
import com.platform.billprocessing.service.RateLimitingService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Extraction Engine", description = "Asynchronous Document Extraction, OCR and Anomaly Evaluation")
public class AiController {

    private final AiBillExtractionService aiBillExtractionService;
    private final RateLimitingService rateLimitingService;

    public AiController(AiBillExtractionService aiBillExtractionService, RateLimitingService rateLimitingService) {
        this.aiBillExtractionService = aiBillExtractionService;
        this.rateLimitingService = rateLimitingService;
    }

    @PostMapping(value = "/extract-bill", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Submit bill PDF or image to async AI processing queue")
    public ResponseEntity<AiJobStatusDTO> extractBill(@RequestParam("file") MultipartFile file) {
        Long tenantId = TenantContext.getTenantId();
        Long userId = TenantContext.getUserId();

        if (!rateLimitingService.tryConsume(tenantId)) {
            throw new RateLimitExceededException("AI endpoint rate limit exceeded for tenant " + tenantId);
        }

        AiJobStatusDTO jobStatus = aiBillExtractionService.initiateAsyncExtraction(file, tenantId, userId);
        return new ResponseEntity<>(jobStatus, HttpStatus.ACCEPTED);
    }

    @GetMapping("/jobs/{jobId}")
    @Operation(summary = "Poll status and extracted payload of an AI extraction job")
    public ResponseEntity<AiJobStatusDTO> getJobStatus(@PathVariable UUID jobId) {
        Long tenantId = TenantContext.getTenantId();
        AiJobStatusDTO job = aiBillExtractionService.getJobStatus(jobId, tenantId);
        return ResponseEntity.ok(job);
    }
}

