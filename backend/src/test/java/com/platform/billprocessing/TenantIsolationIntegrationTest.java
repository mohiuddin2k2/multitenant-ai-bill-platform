package com.platform.billprocessing;

import com.platform.billprocessing.dto.BillDTO;
import com.platform.billprocessing.exception.TenantViolationException;
import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.model.BillStatus;
import com.platform.billprocessing.repository.BillRepository;
import com.platform.billprocessing.service.BillService;
import com.platform.billprocessing.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenantIsolationIntegrationTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private final Long TENANT_NORTH = 2L;
    private final Long TENANT_SOUTH = 3L;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId(TENANT_NORTH);
        TenantContext.setUserRole("CLERK");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Tenant Isolation: North Clerk querying bills must only receive North Tenant's bills")
    void testTenantDataIsolation() {
        Bill northBill1 = Bill.builder().id(101L).tenantId(TENANT_NORTH).vendorName("North Vendor").totalAmount(new BigDecimal("15000")).billDate(LocalDate.now()).status(BillStatus.PENDING).build();
        Bill northBill2 = Bill.builder().id(102L).tenantId(TENANT_NORTH).vendorName("North Power").totalAmount(new BigDecimal("22000")).billDate(LocalDate.now()).status(BillStatus.APPROVED).build();

        when(billRepository.findByTenantId(eq(TENANT_NORTH), any()))
                .thenReturn(new PageImpl<>(List.of(northBill1, northBill2)));

        Page<BillDTO> result = billService.getBills(TENANT_NORTH, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().allMatch(b -> b.getTenantId().equals(TENANT_NORTH)));
        verify(billRepository, never()).findByTenantId(eq(TENANT_SOUTH), any());
    }

    @Test
    @DisplayName("Tenant Security Barrier: Cross-tenant bill access must throw TenantViolationException")
    void testCrossTenantAccessViolation() {
        // Bill 99 belongs to South Tenant (3L)
        Bill southBill = Bill.builder()
                .id(99L)
                .tenantId(TENANT_SOUTH)
                .vendorName("South Leased Line")
                .totalAmount(new BigDecimal("45000"))
                .build();

        when(billRepository.findById(99L)).thenReturn(Optional.of(southBill));

        // North Tenant (2L) attempts to access South Tenant's bill
        TenantViolationException exception = assertThrows(TenantViolationException.class, () -> {
            billService.getBillById(99L, TENANT_NORTH);
        });

        assertTrue(exception.getMessage().contains("Security violation: Tenant 2 is not authorized to access bill 99 belonging to Tenant 3"));
    }
}

