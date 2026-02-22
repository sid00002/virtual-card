package com.virtualcard.virtual_card_platform.integration.repository;

import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


class CardRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private CardRepository repository;

    @Test
    void shouldSaveCard() {

        Card card = Card.builder()
                .cardholderName("John")
                .balance(BigDecimal.valueOf(1000))
                .status(CardStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();

        Card saved = repository.save(card);

        assertNotNull(saved.getId());
    }
}


