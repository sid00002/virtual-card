package com.virtualcard.virtual_card_platform.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI virtualCardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Virtual Card Platform API")
                        .description("Production grade Virtual Card Issuance System")
                        .version("1.0"));
    }
}

