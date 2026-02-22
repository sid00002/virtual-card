package com.virtualcard.virtual_card_platform.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CardResponse {

    private UUID id;
    private String cardholderName;
    private BigDecimal balance;
    private String status;
    private Instant createdAt;

    public CardResponse(UUID id,
                        String cardholderName,
                        BigDecimal balance,
                        String status,
                        Instant createdAt) {
        this.id = id;
        this.cardholderName = cardholderName;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

