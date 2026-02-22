package com.virtualcard.virtual_card_platform.unit.application;

import com.virtualcard.virtual_card_platform.api.dto.request.SpendRequest;
import com.virtualcard.virtual_card_platform.api.dto.request.TopUpRequest;
import com.virtualcard.virtual_card_platform.application.service.IdempotencyService;
import com.virtualcard.virtual_card_platform.application.service.TransactionService;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotActiveException;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotFoundException;
import com.virtualcard.virtual_card_platform.domain.exception.InsufficientFundsException;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionType;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import com.virtualcard.virtual_card_platform.domain.repository.TransactionRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private IdempotencyService idempotencyService;

    private MeterRegistry meterRegistry;
    private TransactionService transactionService;


    private UUID cardId;
    private Card activeCard;

    @BeforeEach
    void setUp() {

        meterRegistry = new io.micrometer.core.instrument.simple.SimpleMeterRegistry();

        transactionService = new TransactionService(
                cardRepository,
                transactionRepository,
                idempotencyService,
                meterRegistry
        );

        cardId = UUID.randomUUID();

        activeCard = Card.builder()
                .id(cardId)
                .cardholderName("Siddhesh")
                .balance(new BigDecimal("1000"))
                .status(CardStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();
    }

    // SPEND - SUCCESS
    @Test
    void spend_shouldDeductBalance_andSaveTransaction() {

        SpendRequest request = new SpendRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("200"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.of(activeCard));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.spend(request);

        assertNotNull(result);
        assertEquals(TransactionType.SPEND, result.getType());
        assertEquals(new BigDecimal("800"), activeCard.getBalance());

        verify(transactionRepository, times(1))
                .save(any(Transaction.class));
    }

    // SPEND - CARD NOT FOUND
    @Test
    void spend_shouldThrowException_whenCardNotFound() {

        SpendRequest request = new SpendRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("100"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> transactionService.spend(request));

        verify(transactionRepository, never()).save(any());
    }

    // SPEND - CARD NOT ACTIVE
    @Test
    void spend_shouldThrowException_whenCardNotActive() {

        activeCard.setStatus(CardStatus.BLOCKED);

        SpendRequest request = new SpendRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("100"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.of(activeCard));

        assertThrows(CardNotActiveException.class,
                () -> transactionService.spend(request));

        verify(transactionRepository, never()).save(any());
    }

    // SPEND - INSUFFICIENT FUNDS
    @Test
    void spend_shouldThrowException_whenInsufficientFunds() {

        SpendRequest request = new SpendRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("2000"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.of(activeCard));

        assertThrows(InsufficientFundsException.class,
                () -> transactionService.spend(request));

        verify(transactionRepository, never()).save(any());
    }

    // TOPUP - SUCCESS
    @Test
    void topUp_shouldIncreaseBalance_andSaveTransaction() {

        TopUpRequest request = new TopUpRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("500"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.of(activeCard));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.topUp(request);

        assertNotNull(result);
        assertEquals(TransactionType.TOPUP, result.getType());
        assertEquals(new BigDecimal("1500"), activeCard.getBalance());

        verify(transactionRepository, times(1))
                .save(any(Transaction.class));
    }

    // TOPUP - CARD NOT ACTIVE
    @Test
    void topUp_shouldThrowException_whenCardNotActive() {

        activeCard.setStatus(CardStatus.BLOCKED);

        TopUpRequest request = new TopUpRequest();
        request.setCardId(cardId);
        request.setAmount(new BigDecimal("500"));

        when(cardRepository.findByIdForUpdate(cardId))
                .thenReturn(Optional.of(activeCard));

        assertThrows(CardNotActiveException.class,
                () -> transactionService.topUp(request));

        verify(transactionRepository, never()).save(any());
    }
}