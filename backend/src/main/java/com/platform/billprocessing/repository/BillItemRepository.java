package com.platform.billprocessing.repository;

import com.platform.billprocessing.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
    List<BillItem> findByBillIdAndTenantId(Long billId, Long tenantId);
    void deleteByBillIdAndTenantId(Long billId, Long tenantId);
}

