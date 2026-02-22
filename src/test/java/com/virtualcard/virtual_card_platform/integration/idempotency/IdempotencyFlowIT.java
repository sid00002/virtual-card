package com.virtualcard.virtual_card_platform.integration.idempotency;


import com.jayway.jsonpath.JsonPath;
import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.config.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class IdempotencyFlowIT extends BaseIntegrationTest {

    @Test
    void shouldReturnSameResponseForSameKey() throws Exception {

        // Create card
        String cardResp = mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.createCardRequest()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String cardId = JsonPath.read(cardResp, "$.data.id");

        String key = "idem-123";

        // First request
        var first = mockMvc.perform(post("/transactions/spend")
                        .header("Idempotency-Key", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.spendRequest(cardId, 100)))
                .andExpect(result -> assertEquals(200, result.getResponse().getStatus()))
                .andReturn();

        // Second request (same key)
        var second = mockMvc.perform(post("/transactions/spend")
                        .header("Idempotency-Key", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.spendRequest(cardId, 100)))
                .andExpect(result -> assertEquals(200, result.getResponse().getStatus()))
                .andReturn();

        // Same response assertion
        assertEquals(
                first.getResponse().getContentAsString(),
                second.getResponse().getContentAsString()
        );
    }
}

