package com.platform.billprocessing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.billprocessing.dto.BillDTO;
import com.platform.billprocessing.dto.FilterCriteriaDTO;
import com.platform.billprocessing.dto.NaturalLanguageQueryResponseDTO;
import com.platform.billprocessing.model.Bill;
import com.platform.billprocessing.model.BillStatus;
import com.platform.billprocessing.repository.BillRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaturalLanguageQueryService {

    private final BillRepository billRepository;
    private final BillService billService;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private ChatClient.Builder chatClientBuilder;

    @Value("${app.ai.mock-fallback-enabled:true}")
    private boolean mockFallbackEnabled;

    public NaturalLanguageQueryResponseDTO executeNaturalLanguageQuery(Long tenantId, String userPrompt) {
        log.info("Processing NL query for Tenant {}: '{}'", tenantId, userPrompt);

        FilterCriteriaDTO criteria = parsePromptToCriteria(userPrompt);
        Specification<Bill> spec = buildSafeSpecification(tenantId, criteria);

        Page<Bill> results = billRepository.findAll(spec, PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "billDate")));
        List<BillDTO> dtoList = results.getContent().stream()
                .map(billService::mapToDTO)
                .collect(Collectors.toList());

        String explanation = generateHumanReadableExplanation(criteria);

        return NaturalLanguageQueryResponseDTO.builder()
                .originalPrompt(userPrompt)
                .explanation(explanation)
                .parsedCriteria(criteria)
                .totalMatches(results.getTotalElements())
                .bills(dtoList)
                .build();
    }

    private FilterCriteriaDTO parsePromptToCriteria(String prompt) {
        if (chatClientBuilder != null && !mockFallbackEnabled) {
            try {
                ChatClient client = chatClientBuilder.build();
                String systemPrompt = """
                        You are a safe SQL/JPA query intent extractor.
                        Convert the user's natural language search request for bills into JSON matching:
                        {
                          "vendorName": string or null,
                          "category": string or null,
                          "status": string (one of: DRAFT, PENDING, IN_REVIEW, APPROVED, REJECTED, PAID) or null,
                          "minAmount": number or null,
                          "maxAmount": number or null,
                          "startDate": "YYYY-MM-DD" or null,
                          "endDate": "YYYY-MM-DD" or null,
                          "isAnomalyOnly": boolean or null
                        }
                        Today's date is: """ + LocalDate.now() + "\nUser prompt: " + prompt;

                String json = client.prompt().user(systemPrompt).call().content();
                return objectMapper.readValue(cleanJson(json), FilterCriteriaDTO.class);
            } catch (Exception e) {
                log.warn("Spring AI parser encountered error, falling back to heuristic parser: {}", e.getMessage());
            }
        }

        return parsePromptHeuristically(prompt);
    }

    private FilterCriteriaDTO parsePromptHeuristically(String prompt) {
        String lower = prompt.toLowerCase();
        FilterCriteriaDTO criteria = new FilterCriteriaDTO();

        // 1. Status extraction
        if (lower.contains("pending")) {
            criteria.setStatus(BillStatus.PENDING);
        } else if (lower.contains("approved")) {
            criteria.setStatus(BillStatus.APPROVED);
        } else if (lower.contains("rejected")) {
            criteria.setStatus(BillStatus.REJECTED);
        } else if (lower.contains("paid")) {
            criteria.setStatus(BillStatus.PAID);
        } else if (lower.contains("in review") || lower.contains("review")) {
            criteria.setStatus(BillStatus.IN_REVIEW);
        }

        // 2. Anomaly flag
        if (lower.contains("anomaly") || lower.contains("anomalies") || lower.contains("duplicate") || lower.contains("suspicious")) {
            criteria.setIsAnomalyOnly(true);
        }

        // 3. Amount extraction (e.g., "over 50,000", "above 50000", "greater than 1 lakh", "more than 100000")
        Pattern amountPattern = Pattern.compile("(?:over|above|greater than|more than|>)\\s*(?:rs\\.?|inr|₹)?\\s*([0-9,]+(?:\\.[0-9]{1,2})?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = amountPattern.matcher(lower);
        if (matcher.find()) {
            String rawNum = matcher.group(1).replace(",", "");
            criteria.setMinAmount(new BigDecimal(rawNum));
        }

        // Handle Indian "lakh" e.g. "over 1 lakh"
        if (lower.contains("1 lakh") || lower.contains("one lakh")) {
            criteria.setMinAmount(new BigDecimal("100000"));
        } else if (lower.contains("5 lakh") || lower.contains("five lakh")) {
            criteria.setMinAmount(new BigDecimal("500000"));
        }

        // 4. Date ranges
        LocalDate now = LocalDate.now();
        if (lower.contains("this month")) {
            criteria.setStartDate(now.with(TemporalAdjusters.firstDayOfMonth()));
            criteria.setEndDate(now.with(TemporalAdjusters.lastDayOfMonth()));
        } else if (lower.contains("last month")) {
            LocalDate lastMonth = now.minusMonths(1);
            criteria.setStartDate(lastMonth.with(TemporalAdjusters.firstDayOfMonth()));
            criteria.setEndDate(lastMonth.with(TemporalAdjusters.lastDayOfMonth()));
        } else if (lower.contains("last 7 days") || lower.contains("past week") || lower.contains("last week")) {
            criteria.setStartDate(now.minusDays(7));
            criteria.setEndDate(now);
        }

        // 5. Vendor / Category extraction
        if (lower.contains("reliance")) {
            criteria.setVendorName("Reliance Power Ltd");
        } else if (lower.contains("tata") || lower.contains("tcs")) {
            criteria.setVendorName("Tata Consultancy Services");
        } else if (lower.contains("dell")) {
            criteria.setVendorName("Dell Technologies India");
        } else if (lower.contains("airtel")) {
            criteria.setVendorName("Airtel Business Fiber");
        } else if (lower.contains("aws") || lower.contains("amazon")) {
            criteria.setVendorName("Amazon Web Services Cloud");
        }

        if (lower.contains("utilities") || lower.contains("electricity") || lower.contains("power")) {
            criteria.setCategory("Utilities");
        } else if (lower.contains("it") || lower.contains("software")) {
            criteria.setCategory("IT Services");
        } else if (lower.contains("hardware") || lower.contains("equipment")) {
            criteria.setCategory("IT Equipment");
        }

        return criteria;
    }

    private Specification<Bill> buildSafeSpecification(Long tenantId, FilterCriteriaDTO criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // STRICT TENANT ISOLATION (MANDATORY LAYER 1 SECURITY)
            predicates.add(cb.equal(root.get("tenantId"), tenantId));

            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }

            if (criteria.getVendorName() != null && !criteria.getVendorName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("vendorName")), "%" + criteria.getVendorName().toLowerCase() + "%"));
            }

            if (criteria.getCategory() != null && !criteria.getCategory().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("category")), "%" + criteria.getCategory().toLowerCase() + "%"));
            }

            if (criteria.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), criteria.getMinAmount()));
            }

            if (criteria.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), criteria.getMaxAmount()));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("billDate"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("billDate"), criteria.getEndDate()));
            }

            if (Boolean.TRUE.equals(criteria.getIsAnomalyOnly())) {
                predicates.add(cb.isTrue(root.get("isAnomaly")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String generateHumanReadableExplanation(FilterCriteriaDTO criteria) {
        List<String> parts = new ArrayList<>();
        if (criteria.getStatus() != null) parts.add("status = " + criteria.getStatus());
        if (criteria.getMinAmount() != null) parts.add("amount >= ₹" + criteria.getMinAmount());
        if (criteria.getMaxAmount() != null) parts.add("amount <= ₹" + criteria.getMaxAmount());
        if (criteria.getVendorName() != null) parts.add("vendor = '" + criteria.getVendorName() + "'");
        if (criteria.getCategory() != null) parts.add("category = '" + criteria.getCategory() + "'");
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            parts.add("between " + criteria.getStartDate() + " and " + criteria.getEndDate());
        }
        if (Boolean.TRUE.equals(criteria.getIsAnomalyOnly())) parts.add("anomaly flags only");

        if (parts.isEmpty()) {
            return "Showing all records within tenant scope";
        }
        return "Filtered by: " + String.join(", ", parts);
    }

    private String cleanJson(String raw) {
        String trimmed = raw.trim();
        if (trimmed.startsWith("```json")) trimmed = trimmed.substring(7);
        if (trimmed.startsWith("```")) trimmed = trimmed.substring(3);
        if (trimmed.endsWith("```")) trimmed = trimmed.substring(0, trimmed.length() - 3);
        return trimmed.trim();
    }
}

