package com.platform.billprocessing.dto;

import java.util.List;

public class NaturalLanguageQueryResponseDTO {
    private String originalPrompt;
    private String explanation;
    private FilterCriteriaDTO parsedCriteria;
    private long totalMatches;
    private List<BillDTO> bills;

    public NaturalLanguageQueryResponseDTO() {
    }

    public NaturalLanguageQueryResponseDTO(String originalPrompt, String explanation, FilterCriteriaDTO parsedCriteria,
                                           long totalMatches, List<BillDTO> bills) {
        this.originalPrompt = originalPrompt;
        this.explanation = explanation;
        this.parsedCriteria = parsedCriteria;
        this.totalMatches = totalMatches;
        this.bills = bills;
    }

    public static NaturalLanguageQueryResponseDTOBuilder builder() {
        return new NaturalLanguageQueryResponseDTOBuilder();
    }

    public String getOriginalPrompt() { return originalPrompt; }
    public void setOriginalPrompt(String originalPrompt) { this.originalPrompt = originalPrompt; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public FilterCriteriaDTO getParsedCriteria() { return parsedCriteria; }
    public void setParsedCriteria(FilterCriteriaDTO parsedCriteria) { this.parsedCriteria = parsedCriteria; }
    public long getTotalMatches() { return totalMatches; }
    public void setTotalMatches(long totalMatches) { this.totalMatches = totalMatches; }
    public List<BillDTO> getBills() { return bills; }
    public void setBills(List<BillDTO> bills) { this.bills = bills; }

    public static class NaturalLanguageQueryResponseDTOBuilder {
        private String originalPrompt;
        private String explanation;
        private FilterCriteriaDTO parsedCriteria;
        private long totalMatches;
        private List<BillDTO> bills;

        public NaturalLanguageQueryResponseDTOBuilder originalPrompt(String originalPrompt) {
            this.originalPrompt = originalPrompt;
            return this;
        }

        public NaturalLanguageQueryResponseDTOBuilder explanation(String explanation) {
            this.explanation = explanation;
            return this;
        }

        public NaturalLanguageQueryResponseDTOBuilder parsedCriteria(FilterCriteriaDTO parsedCriteria) {
            this.parsedCriteria = parsedCriteria;
            return this;
        }

        public NaturalLanguageQueryResponseDTOBuilder totalMatches(long totalMatches) {
            this.totalMatches = totalMatches;
            return this;
        }

        public NaturalLanguageQueryResponseDTOBuilder bills(List<BillDTO> bills) {
            this.bills = bills;
            return this;
        }

        public NaturalLanguageQueryResponseDTO build() {
            return new NaturalLanguageQueryResponseDTO(originalPrompt, explanation, parsedCriteria, totalMatches, bills);
        }
    }
}
