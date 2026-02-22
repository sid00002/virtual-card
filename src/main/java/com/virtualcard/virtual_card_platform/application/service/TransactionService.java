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
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final MeterRegistry meterRegistry;

    @Transactional
    public Transaction spend(SpendRequest request) {

        log.info("Spend request started. cardId={}, amount={}",
                request.getCardId(), request.getAmount());

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            Card card = cardRepository.findByIdForUpdate(request.getCardId())
                    .orElseThrow(() -> {
                        log.error("Card not found for spend. cardId={}", request.getCardId());
                        return new CardNotFoundException();
                    });

            // Validate card status
            if (card.getStatus() != CardStatus.ACTIVE) {
                log.warn("Card not active. cardId={}, status={}",
                        request.getCardId(), card.getStatus());
                throw new CardNotActiveException();
            }

            var amount = request.getAmount();

            // Validate sufficient balance
            if (card.getBalance().compareTo(amount) < 0) {
                log.warn("Insufficient funds. cardId={}, balance={}, requested={}",
                        request.getCardId(), card.getBalance(), amount);

                meterRegistry.counter("transactions.failed",
                        "reason", "insufficient_funds").increment();

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

            meterRegistry.counter("transactions.success").increment();

            log.info("Spend transaction successful. cardId={}, amount={}, txId={}",
                    request.getCardId(), amount, tx.getId());

            return tx;
        }
        catch (RuntimeException ex) {
            log.error("Spend transaction failed. cardId={}, error={}",
                    request.getCardId(), ex.getMessage(), ex);
            throw ex;
        }
        finally {

            sample.stop(
                    Timer.builder("transactions.latency")
                            .description("Transaction processing time")
                            .register(meterRegistry)
            );

            log.info("Spend request completed. cardId={}", request.getCardId());
        }
    }


    @Transactional
    public Transaction topUp(TopUpRequest request) {

        log.info("TopUp request started. cardId={}, amount={}",
                request.getCardId(), request.getAmount());

        Card card = cardRepository.findByIdForUpdate(request.getCardId())
                .orElseThrow(() -> {
                    log.error("Card not found for topUp. cardId={}", request.getCardId());
                    return new CardNotFoundException();
                });

        if (card.getStatus() != CardStatus.ACTIVE) {
            log.warn("Card not active for topUp. cardId={}, status={}",
                    request.getCardId(), card.getStatus());
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

        log.info("TopUp successful. cardId={}, amount={}, txId={}",
                request.getCardId(), amount, tx.getId());

        return tx;
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsByCard(
            UUID cardId,
            Pageable pageable
    ) {

        log.info("Fetching transactions. cardId={}, page={}, size={}",
                cardId, pageable.getPageNumber(), pageable.getPageSize());

        if (!cardRepository.existsById(cardId)) {
            log.error("Card not found while fetching transactions. cardId={}", cardId);
            throw new CardNotFoundException();
        }

        Page<Transaction> page = transactionRepository.findByCardId(cardId, pageable);

        log.info("Transactions fetched successfully. cardId={}, totalElements={}",
                cardId, page.getTotalElements());

        return page;
    }
}