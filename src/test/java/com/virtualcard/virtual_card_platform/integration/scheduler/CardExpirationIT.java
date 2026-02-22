package com.virtualcard.virtual_card_platform.integration.scheduler;

import com.virtualcard.virtual_card_platform.application.service.CardExpirationService;
import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardExpirationIT extends BaseIntegrationTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardExpirationService expirationService;

    @Test
    void shouldExpireCardsSuccessfully() {

        // Create card with past expiry
        Card card = new Card();
        card.setCardholderName("Expired User");
        card.setBalance(BigDecimal.valueOf(1000));
        card.setStatus(CardStatus.ACTIVE);
        card.setCreatedAt(Instant.now());
        card.setExpiryAt(Instant.now().minusSeconds(3600)); // expired 1 hour ago

        card = cardRepository.save(card);

        // Run expiration job
        expirationService.expireCards();

        // Reload card
        Card updated = cardRepository.findById(card.getId()).orElseThrow();

        assertEquals(CardStatus.EXPIRED, updated.getStatus());
    }
}