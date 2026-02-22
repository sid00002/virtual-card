package com.virtualcard.virtual_card_platform.unit.domain;

import com.virtualcard.virtual_card_platform.domain.model.Card;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    @Test
    void shouldUpdateBalanceCorrectly() {

        Card card = new Card();
        card.setBalance(BigDecimal.valueOf(500));

        card.setBalance(
                card.getBalance().subtract(BigDecimal.valueOf(200))
        );

        assertEquals(
                BigDecimal.valueOf(300),
                card.getBalance()
        );
    }
}


