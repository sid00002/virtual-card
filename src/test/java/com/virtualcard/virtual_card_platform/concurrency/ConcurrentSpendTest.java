package com.virtualcard.virtual_card_platform.concurrency;

import com.jayway.jsonpath.JsonPath;
import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.config.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.concurrent.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class ConcurrentSpendTest extends BaseIntegrationTest {

    @Test
    void shouldHandleConcurrentSpend() throws Exception {

        String cardResp = mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.createCardRequest()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String cardId = JsonPath.read(cardResp, "$.data.id");

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        Runnable task = () -> {
            try {
                mockMvc.perform(post("/transactions/spend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.spendRequest(cardId, 1000)));
            } catch (Exception ignored) {
            }
        };

        executor.submit(task);
        executor.submit(task);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
