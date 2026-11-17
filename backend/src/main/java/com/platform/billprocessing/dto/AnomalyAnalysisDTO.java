package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.AnomalySeverity;

import java.math.BigDecimal;
import java.util.List;

public class AnomalyAnalysisDTO {
    private boolean anomaly;
    private AnomalySeverity severity;
    private List<String> anomalyReasons;
    private boolean duplicate;
    private String duplicateInvoiceNumber;
    private BigDecimal historicalAverageAmount;
    private BigDecimal currentAmount;
    private Double deviationMultiplier;

    public AnomalyAnalysisDTO() {
    }

    public AnomalyAnalysisDTO(boolean anomaly, AnomalySeverity severity, List<String> anomalyReasons,
                              boolean duplicate, String duplicateInvoiceNumber, BigDecimal historicalAverageAmount,
                              BigDecimal currentAmount, Double deviationMultiplier) {
        this.anomaly = anomaly;
        this.severity = severity;
        this.anomalyReasons = anomalyReasons;
        this.duplicate = duplicate;
        this.duplicateInvoiceNumber = duplicateInvoiceNumber;
        this.historicalAverageAmount = historicalAverageAmount;
        this.currentAmount = currentAmount;
        this.deviationMultiplier = deviationMultiplier;
    }

    public static AnomalyAnalysisDTOBuilder builder() {
        return new AnomalyAnalysisDTOBuilder();
    }

    public boolean isAnomaly() { return anomaly; }
    public void setAnomaly(boolean anomaly) { this.anomaly = anomaly; }
    public AnomalySeverity getSeverity() { return severity; }
    public void setSeverity(AnomalySeverity severity) { this.severity = severity; }
    public List<String> getAnomalyReasons() { return anomalyReasons; }
    public void setAnomalyReasons(List<String> anomalyReasons) { this.anomalyReasons = anomalyReasons; }
    public boolean isDuplicate() { return duplicate; }
    public void setDuplicate(boolean duplicate) { this.duplicate = duplicate; }
    public String getDuplicateInvoiceNumber() { return duplicateInvoiceNumber; }
    public void setDuplicateInvoiceNumber(String duplicateInvoiceNumber) { this.duplicateInvoiceNumber = duplicateInvoiceNumber; }
    public BigDecimal getHistoricalAverageAmount() { return historicalAverageAmount; }
    public void setHistoricalAverageAmount(BigDecimal historicalAverageAmount) { this.historicalAverageAmount = historicalAverageAmount; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(BigDecimal currentAmount) { this.currentAmount = currentAmount; }
    public Double getDeviationMultiplier() { return deviationMultiplier; }
    public void setDeviationMultiplier(Double deviationMultiplier) { this.deviationMultiplier = deviationMultiplier; }

    public static class AnomalyAnalysisDTOBuilder {
        private boolean anomaly;
        private AnomalySeverity severity;
        private List<String> anomalyReasons;
        private boolean duplicate;
        private String duplicateInvoiceNumber;
        private BigDecimal historicalAverageAmount;
        private BigDecimal currentAmount;
        private Double deviationMultiplier;

        public AnomalyAnalysisDTOBuilder anomaly(boolean anomaly) { this.anomaly = anomaly; return this; }
        public AnomalyAnalysisDTOBuilder severity(AnomalySeverity severity) { this.severity = severity; return this; }
        public AnomalyAnalysisDTOBuilder anomalyReasons(List<String> anomalyReasons) { this.anomalyReasons = anomalyReasons; return this; }
        public AnomalyAnalysisDTOBuilder duplicate(boolean duplicate) { this.duplicate = duplicate; return this; }
        public AnomalyAnalysisDTOBuilder duplicateInvoiceNumber(String duplicateInvoiceNumber) { this.duplicateInvoiceNumber = duplicateInvoiceNumber; return this; }
        public AnomalyAnalysisDTOBuilder historicalAverageAmount(BigDecimal historicalAverageAmount) { this.historicalAverageAmount = historicalAverageAmount; return this; }
        public AnomalyAnalysisDTOBuilder currentAmount(BigDecimal currentAmount) { this.currentAmount = currentAmount; return this; }
        public AnomalyAnalysisDTOBuilder deviationMultiplier(Double deviationMultiplier) { this.deviationMultiplier = deviationMultiplier; return this; }

        public AnomalyAnalysisDTO build() {
            return new AnomalyAnalysisDTO(anomaly, severity, anomalyReasons, duplicate,
                    duplicateInvoiceNumber, historicalAverageAmount, currentAmount, deviationMultiplier);
        }
    }
}
