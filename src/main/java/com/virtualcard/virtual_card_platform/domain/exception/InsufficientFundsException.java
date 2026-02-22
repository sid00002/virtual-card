package com.virtualcard.virtual_card_platform.domain.exception;

public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException() {
        super("INSUFFICIENT_FUNDS", "Insufficient balance");
    }
}

