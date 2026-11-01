package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiJobStatusDTO {
    private UUID jobId;
    private JobStatus status;
    private String filename;
    private ExtractedBillDataDTO extractedData;
    private AnomalyAnalysisDTO anomalyAnalysis;
    private Integer tokensConsumed;
    private BigDecimal estimatedCostUsd;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}

