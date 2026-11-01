package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopVendorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vendorName;
    private long billCount;
    private BigDecimal totalAmount;
}

