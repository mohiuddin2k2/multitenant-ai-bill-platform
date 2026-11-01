package com.platform.billprocessing.repository;

import com.platform.billprocessing.model.BillAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillAuditLogRepository extends JpaRepository<BillAuditLog, Long> {
    List<BillAuditLog> findByBillIdAndTenantIdOrderByTimestampDesc(Long billId, Long tenantId);
}

