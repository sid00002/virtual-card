package com.virtualcard.virtual_card_platform.application.mapper;

import com.virtualcard.virtual_card_platform.api.dto.response.CardResponse;
import com.virtualcard.virtual_card_platform.domain.model.Card;

public class CardMapper {

    private CardMapper() {}

    public static CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getCardholderName(),
                card.getBalance(),
                card.getStatus().name(),
                card.getCreatedAt()
        );
    }
}
