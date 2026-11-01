package com.platform.billprocessing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class BillCreateDTO {
    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotBlank(message = "Vendor name is required")
    private String vendorName;

    private String vendorTaxId;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Bill date is required")
    private LocalDate billDate;

    private LocalDate dueDate;

    @Builder.Default
    private String currency = "INR";

    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    private String notes;
    private String fileUrl;
    private String fileHash;
    private BigDecimal aiConfidenceScore;

    private List<BillItemDTO> items;
}

