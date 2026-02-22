package com.virtualcard.virtual_card_platform.api.controller;

import com.virtualcard.virtual_card_platform.api.dto.request.SpendRequest;
import com.virtualcard.virtual_card_platform.api.dto.request.TopUpRequest;
import com.virtualcard.virtual_card_platform.application.service.IdempotencyService;
import com.virtualcard.virtual_card_platform.application.service.TransactionService;
import com.virtualcard.virtual_card_platform.common.response.ApiResponse;
import com.virtualcard.virtual_card_platform.domain.model.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final IdempotencyService idempotencyService;

    @PostMapping("/spend")
    public ApiResponse<Transaction> spend(
            @RequestHeader(value = "Idempotency-Key", required = false) String key,
            @Valid @RequestBody SpendRequest request) {

        Transaction tx = service.spend(request);

        if (key != null) {
            idempotencyService.save(
                    key,
                    "/transactions/spend",
                    ApiResponse.success(tx),
                    200
            );
        }

        return ApiResponse.success(tx);
    }



    @PostMapping("/topup")
    public ApiResponse<Transaction> topUp(
            @RequestHeader(value = "Idempotency-Key", required = false) String key,
            @Valid @RequestBody TopUpRequest request) {

        Transaction tx = service.topUp(request);
        if (key != null) {
            idempotencyService.save(
                    key,
                    "/transactions/topup",
                    ApiResponse.success(tx),
                    200
            );
        }
        return ApiResponse.success(tx);
    }

    @GetMapping("/{cardId}/transactions")
    public ApiResponse<Page<Transaction>> getTransactions(
            @PathVariable UUID cardId,
            Pageable pageable
    ) {

        Page<Transaction> transactions =
                service.getTransactionsByCard(cardId, pageable);

        return ApiResponse.success(transactions);
    }
}