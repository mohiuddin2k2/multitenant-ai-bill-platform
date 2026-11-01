package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedBillDataDTO {
    private String invoiceNumber;
    private String vendorName;
    private String vendorTaxId;
    private String category;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String currency;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal confidenceScore;
    private List<BillItemDTO> items;
}

