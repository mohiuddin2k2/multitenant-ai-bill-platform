package com.platform.billprocessing.repository;

import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.model.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    Page<Bill> findByTenantId(Long tenantId, Pageable pageable);

    Page<Bill> findByTenantIdAndStatus(Long tenantId, BillStatus status, Pageable pageable);

    Optional<Bill> findByIdAndTenantId(Long id, Long tenantId);

    long countByTenantId(Long tenantId);

    long countByTenantIdAndStatus(Long tenantId, BillStatus status);

    long countByTenantIdAndIsAnomalyTrue(Long tenantId);

    boolean existsByTenantIdAndVendorNameIgnoreCaseAndInvoiceNumberIgnoreCase(Long tenantId, String vendorName, String invoiceNumber);

    Optional<Bill> findFirstByTenantIdAndFileHash(Long tenantId, String fileHash);

    List<Bill> findTop10ByTenantIdAndVendorNameIgnoreCaseOrderByBillDateDesc(Long tenantId, String vendorName);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.tenantId = :tenantId")
    BigDecimal sumTotalSpendByTenantId(@Param("tenantId") Long tenantId);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Bill b WHERE b.tenantId = :tenantId AND b.status = :status")
    BigDecimal sumTotalSpendByTenantIdAndStatus(@Param("tenantId") Long tenantId, @Param("status") BillStatus status);

    @Query("SELECT b.category, COUNT(b), COALESCE(SUM(b.totalAmount), 0) " +
           "FROM Bill b WHERE b.tenantId = :tenantId GROUP BY b.category")
    List<Object[]> getCategoryBreakdownByTenantId(@Param("tenantId") Long tenantId);

    @Query("SELECT b.vendorName, COUNT(b), COALESCE(SUM(b.totalAmount), 0) " +
           "FROM Bill b WHERE b.tenantId = :tenantId GROUP BY b.vendorName ORDER BY SUM(b.totalAmount) DESC")
    List<Object[]> getTopVendorsByTenantId(@Param("tenantId") Long tenantId, Pageable pageable);
}

