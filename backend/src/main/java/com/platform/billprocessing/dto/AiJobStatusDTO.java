package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AiJobStatusDTO {
    private UUID jobId;
    private JobStatus status;
    private String filename;
    private String fileHash;
    private ExtractedBillDataDTO extractedData;
    private AnomalyAnalysisDTO anomalyAnalysis;
    private Integer tokensConsumed;
    private BigDecimal estimatedCostUsd;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public AiJobStatusDTO() {
    }

    public AiJobStatusDTO(UUID jobId, JobStatus status, String filename, ExtractedBillDataDTO extractedData,
                          AnomalyAnalysisDTO anomalyAnalysis, Integer tokensConsumed, BigDecimal estimatedCostUsd,
                          String errorMessage, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.jobId = jobId;
        this.status = status;
        this.filename = filename;
        this.extractedData = extractedData;
        this.anomalyAnalysis = anomalyAnalysis;
        this.tokensConsumed = tokensConsumed;
        this.estimatedCostUsd = estimatedCostUsd;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static AiJobStatusDTOBuilder builder() {
        return new AiJobStatusDTOBuilder();
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
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

    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }

    public ExtractedBillDataDTO getExtractedData() {
        return extractedData;
    }

    public void setExtractedData(ExtractedBillDataDTO extractedData) {
        this.extractedData = extractedData;
    }

    public AnomalyAnalysisDTO getAnomalyAnalysis() {
        return anomalyAnalysis;
    }

    public void setAnomalyAnalysis(AnomalyAnalysisDTO anomalyAnalysis) {
        this.anomalyAnalysis = anomalyAnalysis;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public static class AiJobStatusDTOBuilder {
        private UUID jobId;
        private JobStatus status;
        private String filename;
        private String fileHash;
        private ExtractedBillDataDTO extractedData;
        private AnomalyAnalysisDTO anomalyAnalysis;
        private Integer tokensConsumed;
        private BigDecimal estimatedCostUsd;
        private String errorMessage;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public AiJobStatusDTOBuilder jobId(UUID jobId) {
            this.jobId = jobId;
            return this;
        }

        public AiJobStatusDTOBuilder status(JobStatus status) {
            this.status = status;
            return this;
        }

        public AiJobStatusDTOBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public AiJobStatusDTOBuilder fileHash(String fileHash) {
            this.fileHash = fileHash;
            return this;
        }

        public AiJobStatusDTOBuilder extractedData(ExtractedBillDataDTO extractedData) {
            this.extractedData = extractedData;
            return this;
        }

        public AiJobStatusDTOBuilder anomalyAnalysis(AnomalyAnalysisDTO anomalyAnalysis) {
            this.anomalyAnalysis = anomalyAnalysis;
            return this;
        }

        public AiJobStatusDTOBuilder tokensConsumed(Integer tokensConsumed) {
            this.tokensConsumed = tokensConsumed;
            return this;
        }

        public AiJobStatusDTOBuilder estimatedCostUsd(BigDecimal estimatedCostUsd) {
            this.estimatedCostUsd = estimatedCostUsd;
            return this;
        }

        public AiJobStatusDTOBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public AiJobStatusDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AiJobStatusDTOBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public AiJobStatusDTO build() {
            AiJobStatusDTO dto = new AiJobStatusDTO(jobId, status, filename, extractedData, anomalyAnalysis,
                    tokensConsumed, estimatedCostUsd, errorMessage, createdAt, completedAt);
            dto.setFileHash(fileHash);
            return dto;
        }
    }
}
