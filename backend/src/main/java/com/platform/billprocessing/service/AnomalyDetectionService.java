package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.AnomalyAnalysisDTO;
import com.platform.billprocessing.model.AnomalySeverity;
import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AnomalyDetectionService {

    private static final Logger log = LoggerFactory.getLogger(AnomalyDetectionService.class);

    private final BillRepository billRepository;

    public AnomalyDetectionService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public AnomalyAnalysisDTO analyzeBill(Long tenantId, String vendorName, String invoiceNumber, BigDecimal totalAmount, String fileHash) {
        List<String> reasons = new ArrayList<>();
        boolean isAnomaly = false;
        boolean isDuplicate = false;
        String duplicateInvoice = null;
        AnomalySeverity highestSeverity = null;

        // 1. Exact Duplicate by Vendor Name + Invoice Number
        if (vendorName != null && invoiceNumber != null) {
            boolean exists = billRepository.existsByTenantIdAndVendorNameIgnoreCaseAndInvoiceNumberIgnoreCase(tenantId, vendorName.trim(), invoiceNumber.trim());
            if (exists) {
                isAnomaly = true;
                isDuplicate = true;
                duplicateInvoice = invoiceNumber.trim();
                reasons.add("Duplicate invoice detected: Vendor '" + vendorName + "' already has a recorded bill with invoice number #" + invoiceNumber);
                highestSeverity = AnomalySeverity.CRITICAL;
            }
        }

        // 2. Binary Duplicate by File Hash (SHA-256)
        if (fileHash != null && !fileHash.isBlank()) {
            Optional<Bill> existingHashBill = billRepository.findFirstByTenantIdAndFileHash(tenantId, fileHash);
            if (existingHashBill.isPresent() && !isDuplicate) {
                isAnomaly = true;
                isDuplicate = true;
                duplicateInvoice = existingHashBill.get().getInvoiceNumber();
                reasons.add("Identical file hash match with existing bill #" + duplicateInvoice + " (Uploaded: " + existingHashBill.get().getCreatedAt() + ")");
                highestSeverity = AnomalySeverity.CRITICAL;
            }
        }

        // 3. Historical Vendor Statistical Outlier Detection
        BigDecimal historicalAvg = null;
        Double deviationMultiplier = null;
        if (vendorName != null && totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            List<Bill> pastBills = billRepository.findTop10ByTenantIdAndVendorNameIgnoreCaseOrderByBillDateDesc(tenantId, vendorName.trim());
            if (!pastBills.isEmpty()) {
                BigDecimal sum = pastBills.stream()
                        .map(Bill::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                historicalAvg = sum.divide(BigDecimal.valueOf(pastBills.size()), 2, RoundingMode.HALF_UP);

                if (historicalAvg.compareTo(BigDecimal.ZERO) > 0) {
                    deviationMultiplier = totalAmount.divide(historicalAvg, 2, RoundingMode.HALF_UP).doubleValue();

                    if (deviationMultiplier >= 3.0) {
                        isAnomaly = true;
                        reasons.add("Amount deviation outlier: ₹" + totalAmount + " is " + deviationMultiplier + "x higher than the vendor's average ticket size of ₹" + historicalAvg);
                        if (highestSeverity == null || highestSeverity.ordinal() < AnomalySeverity.HIGH.ordinal()) {
                            highestSeverity = AnomalySeverity.HIGH;
                        }
                    } else if (deviationMultiplier <= 0.15) {
                        isAnomaly = true;
                        reasons.add("Abnormally low amount: ₹" + totalAmount + " is only " + (deviationMultiplier * 100) + "% of historical vendor average (₹" + historicalAvg + ")");
                        if (highestSeverity == null || highestSeverity.ordinal() < AnomalySeverity.MEDIUM.ordinal()) {
                            highestSeverity = AnomalySeverity.MEDIUM;
                        }
                    }
                }
            }
        }

        // 4. Large Ticket Threshold (Approval Escalation Rule: > ₹500,000)
        if (totalAmount != null && totalAmount.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            isAnomaly = true;
            reasons.add("High-value transaction: Bill exceeds ₹500,000 threshold, mandating secondary executive review");
            if (highestSeverity == null || highestSeverity.ordinal() < AnomalySeverity.HIGH.ordinal()) {
                highestSeverity = AnomalySeverity.HIGH;
            }
        }

        // 5. Suspicious Round Number Check (e.g. ₹100,000.00 without any cents/tax breakup)
        if (totalAmount != null && totalAmount.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            long cents = totalAmount.remainder(BigDecimal.valueOf(10000)).longValue();
            if (cents == 0) {
                isAnomaly = true;
                reasons.add("Suspicious round amount: Exactly ₹" + totalAmount + " without expected statutory taxes or variable unit cents");
                if (highestSeverity == null) {
                    highestSeverity = AnomalySeverity.LOW;
                }
            }
        }

        return AnomalyAnalysisDTO.builder()
                .anomaly(isAnomaly)
                .severity(highestSeverity)
                .anomalyReasons(reasons)
                .duplicate(isDuplicate)
                .duplicateInvoiceNumber(duplicateInvoice)
                .historicalAverageAmount(historicalAvg)
                .currentAmount(totalAmount)
                .deviationMultiplier(deviationMultiplier)
                .build();
    }
}

