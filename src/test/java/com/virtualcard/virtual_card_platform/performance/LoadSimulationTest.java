package com.virtualcard.virtual_card_platform.performance;


import com.virtualcard.virtual_card_platform.config.BaseIntegrationTest;
import com.virtualcard.virtual_card_platform.config.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoadSimulationTest extends BaseIntegrationTest {

    @Test
    void simulateLoad() throws Exception {

        for (int i = 0; i < 20; i++) {

            mockMvc.perform(post("/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestDataFactory.createCardRequest()))
                    .andExpect(status().isOk());
        }
    }
}

