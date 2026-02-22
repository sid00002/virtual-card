package com.virtualcard.virtual_card_platform.unit.domain;


import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {

    @Test
    void shouldCreateTransaction() {

        Transaction tx = Transaction.builder()
                .amount(BigDecimal.valueOf(100))
                .type(TransactionType.SPEND)
                .build();

        assertEquals(
                BigDecimal.valueOf(100),
                tx.getAmount()
        );
    }
}
