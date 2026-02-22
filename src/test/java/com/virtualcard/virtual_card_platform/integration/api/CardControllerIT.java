package com.virtualcard.virtual_card_platform.integration.api;


import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.config.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerIT extends BaseIntegrationTest {

    @Test
    void shouldCreateCard() throws Exception {

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.createCardRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }
}
