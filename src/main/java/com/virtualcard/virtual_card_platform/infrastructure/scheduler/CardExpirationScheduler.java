package com.virtualcard.virtual_card_platform.infrastructure.scheduler;

import com.virtualcard.virtual_card_platform.application.service.CardExpirationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardExpirationScheduler {

    private final CardExpirationService expirationService;

    @Scheduled(fixedRate = 60000)
    public void runExpirationJob() {

        log.info("Running card expiration scheduler");

        expirationService.expireCards();
    }
}