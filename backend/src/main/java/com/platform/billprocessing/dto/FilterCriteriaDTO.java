package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteriaDTO {
    private String vendorName;
    private String category;
    private BillStatus status;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isAnomalyOnly;
}

