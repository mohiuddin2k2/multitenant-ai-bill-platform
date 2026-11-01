package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaturalLanguageQueryResponseDTO {
    private String originalPrompt;
    private String explanation;
    private FilterCriteriaDTO parsedCriteria;
    private long totalMatches;
    private List<BillDTO> bills;
}

