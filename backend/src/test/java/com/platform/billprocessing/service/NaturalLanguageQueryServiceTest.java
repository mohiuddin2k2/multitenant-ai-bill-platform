package com.platform.billprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.billprocessing.dto.NaturalLanguageQueryResponseDTO;
import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.model.BillStatus;
import com.platform.billprocessing.repository.BillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NaturalLanguageQueryServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillService billService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private NaturalLanguageQueryService nlQueryService;

    @Test
    @DisplayName("Should parse 'pending bills over 50,000 this month' into correct filter criteria")
    void shouldParsePendingBillsOver50k() {
        Bill sampleBill = Bill.builder()
                .id(1L)
                .tenantId(2L)
                .vendorName("Reliance Power Ltd")
                .totalAmount(new BigDecimal("80712.00"))
                .status(BillStatus.PENDING)
                .build();

        when(billRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleBill)));

        NaturalLanguageQueryResponseDTO response = nlQueryService.executeNaturalLanguageQuery(
                2L, "pending bills over 50,000 this month");

        assertNotNull(response);
        assertEquals(BillStatus.PENDING, response.getParsedCriteria().getStatus());
        assertEquals(new BigDecimal("50000"), response.getParsedCriteria().getMinAmount());
        assertNotNull(response.getParsedCriteria().getStartDate());
        assertNotNull(response.getParsedCriteria().getEndDate());
        assertTrue(response.getExplanation().contains("status = PENDING"));
        assertTrue(response.getExplanation().contains("amount >= ₹50000"));
    }
}

