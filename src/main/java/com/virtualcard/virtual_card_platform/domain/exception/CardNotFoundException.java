package com.virtualcard.virtual_card_platform.domain.exception;

public class CardNotFoundException extends DomainException {

    public CardNotFoundException() {
        super("CARD_NOT_FOUND", "Card not found");
    }
}
