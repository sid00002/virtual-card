package com.virtualcard.virtual_card_platform.application.service;

import com.virtualcard.virtual_card_platform.api.dto.request.CreateCardRequest;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotFoundException;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    public Card createCard(CreateCardRequest request) {

        log.info("Create card request started. cardholderName={}, initialBalance={}",
                request.getCardholderName(), request.getInitialBalance());

        Card card = new Card();
        card.setCardholderName(request.getCardholderName());
        card.setBalance(request.getInitialBalance());
        card.setStatus(CardStatus.ACTIVE);
        card.setCreatedAt(Instant.now());
        card.setExpiryAt(Instant.now().plusSeconds(60L * 60 * 24 * 365)); // 1 year
        Card saved = cardRepository.save(card);

        log.info("Card created successfully. cardId={}, cardholderName={}",
                saved.getId(), saved.getCardholderName());

        return saved;
    }


    public Card get(UUID id) {

        log.info("Fetching card by id. cardId={}", id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Card not found. cardId={}", id);
                    return new CardNotFoundException();
                });

        log.info("Card fetched successfully. cardId={}, status={}, balance={}",
                card.getId(), card.getStatus(), card.getBalance());

        return card;
    }

    public List<Card> getAll() {

        log.info("Fetching all cards");

        List<Card> cards = cardRepository.findAll();

        log.info("Cards fetched successfully. totalCards={}", cards.size());

        return cards;
    }
}