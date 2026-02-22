package com.virtualcard.virtual_card_platform.unit.application;

import com.virtualcard.virtual_card_platform.api.dto.request.CreateCardRequest;
import com.virtualcard.virtual_card_platform.application.service.CardService;
import com.virtualcard.virtual_card_platform.domain.exception.CardNotFoundException;
import com.virtualcard.virtual_card_platform.domain.model.Card;
import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import com.virtualcard.virtual_card_platform.domain.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private UUID cardId;

    @BeforeEach
    void setUp() {
        cardId = UUID.randomUUID();
    }

    // ==========================================
    // CREATE CARD - SUCCESS
    // ==========================================
    @Test
    void createCard_shouldCreateAndSaveCard() {

        CreateCardRequest request = new CreateCardRequest();
        request.setCardholderName("Siddhesh");
        request.setInitialBalance(new BigDecimal("1000"));

        when(cardRepository.save(any(Card.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Card savedCard = cardService.createCard(request);

        assertNotNull(savedCard);
        assertEquals("Siddhesh", savedCard.getCardholderName());
        assertEquals(new BigDecimal("1000"), savedCard.getBalance());
        assertEquals(CardStatus.ACTIVE, savedCard.getStatus());
        assertNotNull(savedCard.getCreatedAt());

        verify(cardRepository, times(1)).save(any(Card.class));
    }

    // ==========================================
    // GET CARD - SUCCESS
    // ==========================================
    @Test
    void get_shouldReturnCard_whenExists() {

        Card card = Card.builder()
                .id(cardId)
                .cardholderName("Siddhesh")
                .balance(new BigDecimal("1000"))
                .status(CardStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        Card result = cardService.get(cardId);

        assertNotNull(result);
        assertEquals(cardId, result.getId());

        verify(cardRepository, times(1)).findById(cardId);
    }

    // ==========================================
    // GET CARD - NOT FOUND
    // ==========================================
    @Test
    void get_shouldThrowException_whenCardNotFound() {

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> cardService.get(cardId));

        verify(cardRepository, times(1)).findById(cardId);
    }

    // ==========================================
    // GET ALL CARDS
    // ==========================================
    @Test
    void getAll_shouldReturnListOfCards() {

        List<Card> cards = List.of(
                Card.builder()
                        .id(UUID.randomUUID())
                        .cardholderName("User1")
                        .balance(new BigDecimal("500"))
                        .status(CardStatus.ACTIVE)
                        .createdAt(Instant.now())
                        .build(),
                Card.builder()
                        .id(UUID.randomUUID())
                        .cardholderName("User2")
                        .balance(new BigDecimal("700"))
                        .status(CardStatus.ACTIVE)
                        .createdAt(Instant.now())
                        .build()
        );

        when(cardRepository.findAll()).thenReturn(cards);

        List<Card> result = cardService.getAll();

        assertEquals(2, result.size());

        verify(cardRepository, times(1)).findAll();
    }
}
