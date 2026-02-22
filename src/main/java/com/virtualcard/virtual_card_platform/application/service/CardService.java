package com.virtualcard.virtual_card_platform.application.service;

import com.virtualcard.virtual_card_platform.api.dto.request.CreateCardRequest;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotFoundException;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card createCard(CreateCardRequest request) {

        Card card = new Card();
        card.setCardholderName(request.getCardholderName());
        card.setBalance(request.getInitialBalance());
        card.setStatus(CardStatus.ACTIVE);
        card.setCreatedAt(Instant.now());

        return cardRepository.save(card);
    }


    public Card get(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(CardNotFoundException::new);

    }

    public List<Card> getAll() {
        return cardRepository.findAll();
    }
}
