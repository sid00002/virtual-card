package com.virtualcard.virtual_card_platform.domain.repository;

import com.virtualcard.virtual_card_platform.domain.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository
        extends JpaRepository<IdempotencyRecord, UUID> {

    Optional<IdempotencyRecord> findByIdempotencyKeyAndEndpoint(
            String key,
            String endpoint
    );
}

