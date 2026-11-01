package com.platform.billprocessing.repository;

import com.platform.billprocessing.model.AiExtractionJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AiExtractionJobRepository extends JpaRepository<AiExtractionJob, UUID> {
    Optional<AiExtractionJob> findByIdAndTenantId(UUID id, Long tenantId);
    Page<AiExtractionJob> findByTenantIdOrderByCreatedAtDesc(Long tenantId, Pageable pageable);
}

