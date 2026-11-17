package com.platform.billprocessing.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_extraction_jobs")
public class AiExtractionJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobStatus status = JobStatus.PENDING;

    @Column(length = 255)
    private String filename;

    @Column(name = "extracted_data_json", columnDefinition = "TEXT")
    private String extractedDataJson;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "tokens_consumed")
    private Integer tokensConsumed = 0;

    @Column(name = "estimated_cost_usd", precision = 8, scale = 4)
    private BigDecimal estimatedCostUsd = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public AiExtractionJob() {
    }

    public AiExtractionJob(UUID id, Long tenantId, Long userId, JobStatus status, String filename,
                           String extractedDataJson, String errorMessage, Integer tokensConsumed,
                           BigDecimal estimatedCostUsd, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.userId = userId;
        this.status = status != null ? status : JobStatus.PENDING;
        this.filename = filename;
        this.extractedDataJson = extractedDataJson;
        this.errorMessage = errorMessage;
        this.tokensConsumed = tokensConsumed != null ? tokensConsumed : 0;
        this.estimatedCostUsd = estimatedCostUsd != null ? estimatedCostUsd : BigDecimal.ZERO;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static AiExtractionJobBuilder builder() {
        return new AiExtractionJobBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtractedDataJson() {
        return extractedDataJson;
    }

    public void setExtractedDataJson(String extractedDataJson) {
        this.extractedDataJson = extractedDataJson;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getTokensConsumed() {
        return tokensConsumed;
    }

    public void setTokensConsumed(Integer tokensConsumed) {
        this.tokensConsumed = tokensConsumed;
    }

    public BigDecimal getEstimatedCostUsd() {
        return estimatedCostUsd;
    }

    public void setEstimatedCostUsd(BigDecimal estimatedCostUsd) {
        this.estimatedCostUsd = estimatedCostUsd;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public static class AiExtractionJobBuilder {
        private UUID id;
        private Long tenantId;
        private Long userId;
        private JobStatus status = JobStatus.PENDING;
        private String filename;
        private String extractedDataJson;
        private String errorMessage;
        private Integer tokensConsumed = 0;
        private BigDecimal estimatedCostUsd = BigDecimal.ZERO;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public AiExtractionJobBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AiExtractionJobBuilder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public AiExtractionJobBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AiExtractionJobBuilder status(JobStatus status) {
            this.status = status;
            return this;
        }

        public AiExtractionJobBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public AiExtractionJobBuilder extractedDataJson(String extractedDataJson) {
            this.extractedDataJson = extractedDataJson;
            return this;
        }

        public AiExtractionJobBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AiExtractionJobBuilder tokensConsumed(Integer tokensConsumed) {
            this.tokensConsumed = tokensConsumed;
            return this;
        }

        public AiExtractionJobBuilder estimatedCostUsd(BigDecimal estimatedCostUsd) {
            this.estimatedCostUsd = estimatedCostUsd;
            return this;
        }

        public AiExtractionJobBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AiExtractionJobBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public AiExtractionJob build() {
            return new AiExtractionJob(id, tenantId, userId, status, filename, extractedDataJson,
                    errorMessage, tokensConsumed, estimatedCostUsd, createdAt, completedAt);
        }
    }
}
