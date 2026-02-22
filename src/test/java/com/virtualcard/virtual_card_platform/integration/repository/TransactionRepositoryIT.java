package com.virtualcard.virtual_card_platform.integration.repository;


import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionStatus;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionType;
import com.virtualcard.virtual_card_platform.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    void shouldSaveTransaction() {

        Transaction tx = Transaction.builder()
                .cardId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .type(TransactionType.SPEND)
                .status(TransactionStatus.SUCCESS)
                .createdAt(Instant.now())
                .build();

        Transaction saved = repository.save(tx);

        assertNotNull(saved.getId());
    }
}
