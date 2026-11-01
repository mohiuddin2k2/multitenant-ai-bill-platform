package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.AnomalyAnalysisDTO;
import com.platform.billprocessing.model.AnomalySeverity;
import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnomalyDetectionServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private AnomalyDetectionService anomalyDetectionService;

    private Long tenantId;

    @BeforeEach
    void setUp() {
        tenantId = 2L;
    }

    @Test
    @DisplayName("Should detect duplicate invoice with same vendor and invoice number")
    void shouldDetectDuplicateInvoice() {
        when(billRepository.existsByTenantIdAndVendorNameIgnoreCaseAndInvoiceNumberIgnoreCase(
                tenantId, "Reliance Power Ltd", "INV-2026-1044")).thenReturn(true);

        AnomalyAnalysisDTO result = anomalyDetectionService.analyzeBill(
                tenantId, "Reliance Power Ltd", "INV-2026-1044", new BigDecimal("80000.00"), null);

        assertTrue(result.isAnomaly());
        assertTrue(result.isDuplicate());
        assertEquals("INV-2026-1044", result.getDuplicateInvoiceNumber());
        assertEquals(AnomalySeverity.CRITICAL, result.getSeverity());
    }

    @Test
    @DisplayName("Should detect duplicate file hash match")
    void shouldDetectDuplicateFileHash() {
        String testHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        Bill existingBill = Bill.builder()
                .invoiceNumber("INV-EXISTING-99")
                .tenantId(tenantId)
                .build();

        when(billRepository.findFirstByTenantIdAndFileHash(tenantId, testHash))
                .thenReturn(Optional.of(existingBill));

        AnomalyAnalysisDTO result = anomalyDetectionService.analyzeBill(
                tenantId, "Unique Vendor", "INV-NEW-01", new BigDecimal("10000.00"), testHash);

        assertTrue(result.isAnomaly());
        assertTrue(result.isDuplicate());
        assertEquals(AnomalySeverity.CRITICAL, result.getSeverity());
    }

    @Test
    @DisplayName("Should flag statistical outlier when bill is 3x or higher than historical vendor average")
    void shouldFlagStatisticalOutlier() {
        Bill past1 = Bill.builder().totalAmount(new BigDecimal("10000.00")).build();
        Bill past2 = Bill.builder().totalAmount(new BigDecimal("12000.00")).build();
        when(billRepository.findTop10ByTenantIdAndVendorNameIgnoreCaseOrderByBillDateDesc(tenantId, "Office Supplies Inc"))
                .thenReturn(List.of(past1, past2));

        // Average is 11,000; submitting 45,000 (> 4x)
        AnomalyAnalysisDTO result = anomalyDetectionService.analyzeBill(
                tenantId, "Office Supplies Inc", "INV-8899", new BigDecimal("45000.00"), null);

        assertTrue(result.isAnomaly());
        assertFalse(result.isDuplicate());
        assertEquals(AnomalySeverity.HIGH, result.getSeverity());
        assertTrue(result.getDeviationMultiplier() > 3.0);
    }

    @Test
    @DisplayName("Should flag high-value transaction exceeding ₹500,000 threshold")
    void shouldFlagHighValueThreshold() {
        AnomalyAnalysisDTO result = anomalyDetectionService.analyzeBill(
                tenantId, "Enterprise Vendor", "INV-9999", new BigDecimal("750000.00"), null);

        assertTrue(result.isAnomaly());
        assertEquals(AnomalySeverity.HIGH, result.getSeverity());
        assertTrue(result.getAnomalyReasons().stream().anyMatch(r -> r.contains("500,000")));
    }
}

