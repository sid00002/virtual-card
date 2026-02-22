package com.virtualcard.virtual_card_platform.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualcard.virtual_card_platform.domain.model.IdempotencyRecord;
import com.virtualcard.virtual_card_platform.domain.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;

    public Optional<IdempotencyRecord> find(String key, String endpoint) {
        return repository.findByIdempotencyKeyAndEndpoint(key, endpoint);
    }

    public void save(String key,
                     String endpoint,
                     Object response,
                     int status) {

        String body = serialize(response);

        IdempotencyRecord record =
                IdempotencyRecord.builder()
                        .idempotencyKey(key)
                        .endpoint(endpoint)
                        .responseBody(body)
                        .httpStatus(status)
                        .createdAt(Instant.now())
                        .build();

        try {
            repository.save(record);
        } catch (DataIntegrityViolationException ex) {
            // Concurrent request already saved
            log.warn("Duplicate idempotency detected for key={}", key);
        }
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }
}
