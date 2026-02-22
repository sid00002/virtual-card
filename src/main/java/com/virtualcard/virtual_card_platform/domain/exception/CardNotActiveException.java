package com.virtualcard.virtual_card_platform.domain.exception;


public class CardNotActiveException extends DomainException {

    public CardNotActiveException() {
        super("CARD_NOT_ACTIVE", "Card is not active");
    }
}



