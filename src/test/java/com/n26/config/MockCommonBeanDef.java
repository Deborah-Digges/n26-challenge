package com.n26.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import com.n26.resource.TransactionResource;
import com.n26.service.TransactionService;
import com.n26.validation.TransactionValidator;

import static org.mockito.Mockito.mock;

/**
 * @author ddigges
 */
@Configuration
@ActiveProfiles("dev")
public class MockCommonBeanDef {
    @Bean
    @Primary
    public TransactionValidator transactionValidator() {
        return new TransactionValidator();
    }

    @Bean
    @Primary
    public TransactionService transactionService() {
        return mock(TransactionService.class);
    }

    @Bean
    @Primary
    public TransactionResource transactionResource() {
        return new TransactionResource();
    }
}
