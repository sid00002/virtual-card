package com.virtualcard.virtual_card_platform.application.command;

import java.math.BigDecimal;

public class CreateCardCommand {

    private final String cardholderName;
    private final BigDecimal initialBalance;

    public CreateCardCommand(String cardholderName, BigDecimal initialBalance) {
        this.cardholderName = cardholderName;
        this.initialBalance = initialBalance;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

}