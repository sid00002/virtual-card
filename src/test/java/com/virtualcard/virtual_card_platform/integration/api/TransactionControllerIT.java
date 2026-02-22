package com.virtualcard.virtual_card_platform.integration.api;


import com.jayway.jsonpath.JsonPath;
import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.config.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class TransactionControllerIT extends BaseIntegrationTest {

    @Test
    void shouldSpendSuccessfully() throws Exception {

        String cardResp = mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.createCardRequest()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String cardId = JsonPath.read(cardResp, "$.data.id");

        mockMvc.perform(post("/transactions/spend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.spendRequest(cardId, 100)))
                .andReturn();
    }
}

