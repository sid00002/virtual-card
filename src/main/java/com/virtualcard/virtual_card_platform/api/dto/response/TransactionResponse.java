package com.virtualcard.virtual_card_platform.api.dto.response;

import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {

    private UUID transactionId;
    private UUID cardId;
    private BigDecimal amount;
    private TransactionType type;
    private Instant createdAt;
}
