package com.virtualcard.virtual_card_platform.config;


public class TestDataFactory {

    public static String createCardRequest() {
        return """
                {
                  "cardholderName": "John Doe",
                  "initialBalance": 1000
                }
                """;
    }

    public static String spendRequest(String cardId, int amount) {
        return """
                {
                  "cardId": "%s",
                  "amount": %d
                }
                """.formatted(cardId, amount);
    }
}


