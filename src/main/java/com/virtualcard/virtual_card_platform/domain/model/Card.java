package com.virtualcard.virtual_card_platform.domain.model;



import com.virtualcard.virtual_card_platform.domain.model.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String cardholderName;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(name = "expiry_at")
    private Instant expiryAt;
}
