package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.AnomalySeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyAnalysisDTO {
    private boolean anomaly;
    private AnomalySeverity severity;
    private List<String> anomalyReasons;
    private boolean duplicate;
    private String duplicateInvoiceNumber;
    private BigDecimal historicalAverageAmount;
    private BigDecimal currentAmount;
    private Double deviationMultiplier;
}

