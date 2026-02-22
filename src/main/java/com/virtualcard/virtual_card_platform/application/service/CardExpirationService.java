package com.virtualcard.virtual_card_platform.application.service;

import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardExpirationService {

    private final CardRepository cardRepository;

    @Transactional
    public void expireCards() {

        Instant now = Instant.now();

        List<Card> expiredCards =
                cardRepository.findByStatusAndExpiryAtBefore(CardStatus.ACTIVE, now);

        if (expiredCards.isEmpty()) {
            log.info("No cards to expire");
            return;
        }

        for (Card card : expiredCards) {
            card.setStatus(CardStatus.EXPIRED);
            log.info("Card expired. cardId={}", card.getId());
        }

        log.info("Card expiration job completed. totalExpired={}", expiredCards.size());
    }
}