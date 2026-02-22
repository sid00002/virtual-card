package com.virtualcard.virtual_card_platform.api.controller;

import com.virtualcard.virtual_card_platform.api.dto.request.CreateCardRequest;
import com.virtualcard.virtual_card_platform.api.dto.response.CardResponse;
import com.virtualcard.virtual_card_platform.application.mapper.CardMapper;
import com.virtualcard.virtual_card_platform.application.service.CardService;
import com.virtualcard.virtual_card_platform.common.response.ApiResponse;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService service;

    @PostMapping
    public ApiResponse<CardResponse> create(
            @Valid @RequestBody CreateCardRequest request) {

        Card card = service.createCard(request);
        CardResponse response = CardMapper.toResponse(card);

        return ApiResponse.success(response);
    }


    @GetMapping("/{id}")
    public Card get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping
    public List<Card> getAll() {
        return service.getAll();
    }
}