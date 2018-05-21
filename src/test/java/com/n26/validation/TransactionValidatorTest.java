package com.n26.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.common.Constants;
import com.n26.entity.rest.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TransactionValidatorTest {
    private TransactionValidator transactionValidator = new TransactionValidator();

    @Test
    public void test_transactionValidator_validTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(System.currentTimeMillis()/1000);

        boolean isValid = transactionValidator.validateTransaction(transaction, Constants.ONE_MINUTE);
        assertThat(isValid).isTrue();
    }

    @Test
    public void test_transactionValidator_invalidTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(System.currentTimeMillis()/1000 - (Constants.ONE_MINUTE + 1));

        boolean isValid = transactionValidator.validateTransaction(transaction, Constants.ONE_MINUTE);
        assertThat(isValid).isFalse();
    }
}
