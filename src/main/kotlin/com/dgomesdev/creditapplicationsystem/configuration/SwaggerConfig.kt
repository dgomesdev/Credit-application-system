package com.dgomesdev.creditapplicationsystem.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun setupPublicApi(): GroupedOpenApi? =
        GroupedOpenApi.builder()
            .group("springCreditApplicationSystem-public")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
}