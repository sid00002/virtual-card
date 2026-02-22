package com.virtualcard.virtual_card_platform.application.service;

import com.virtualcard.virtual_card_platform.api.dto.request.SpendRequest;
import com.virtualcard.virtual_card_platform.api.dto.request.TopUpRequest;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotActiveException;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotFoundException;
import com.virtualcard.virtual_card_platform.domain.exception.InsufficientFundsException;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionStatus;
import com.virtualcard.virtual_card_platform.domain.model.enums.TransactionType;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import com.virtualcard.virtual_card_platform.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyService idempotencyService;

    @Transactional
    public Transaction spend(SpendRequest request) {

        Card card = cardRepository.findByIdForUpdate(request.getCardId())
                .orElseThrow(CardNotFoundException::new);

        // Validate card status
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardNotActiveException();
        }

        var amount = request.getAmount();

        // Validate sufficient balance
        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        // Deduct balance
        card.setBalance(card.getBalance().subtract(amount));

        // Create transaction record
        Transaction tx = Transaction.builder()
                .cardId(card.getId())
                .amount(amount)
                .type(TransactionType.SPEND)
                .status(TransactionStatus.SUCCESS)
                .createdAt(Instant.now())
                .build();

        transactionRepository.save(tx);

        return tx;
    }



    @Transactional
    public Transaction topUp(TopUpRequest request) {

        Card card = cardRepository.findByIdForUpdate(request.getCardId())
                .orElseThrow(CardNotFoundException::new);

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardNotActiveException();
        }

        BigDecimal amount = request.getAmount();

        card.setBalance(card.getBalance().add(amount));

        Transaction tx = Transaction.builder()
                .cardId(card.getId())
                .amount(amount)
                .type(TransactionType.TOPUP)
                .status(TransactionStatus.SUCCESS)
                .createdAt(Instant.now())
                .build();

        transactionRepository.save(tx);

        return tx;
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByCard(
            UUID cardId,
            Pageable pageable
    ) {

        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException();
        }

        return transactionRepository.findByCardId(cardId, pageable);
    }
}