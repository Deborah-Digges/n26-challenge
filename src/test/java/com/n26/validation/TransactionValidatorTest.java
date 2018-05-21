package com.n26.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.common.Constants;
import com.n26.common.TransactionTestUtils;
import com.n26.entity.rest.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TransactionValidatorTest {
    private TransactionValidator transactionValidator = new TransactionValidator();
    private TransactionTestUtils transactionTestUtils = new TransactionTestUtils();

    @Test
    public void test_transactionValidator_validTransaction() {
        Transaction transaction = transactionTestUtils.createTransactionInWindow(100);

        boolean isValid = transactionValidator.validateTransaction(transaction, Constants.ONE_MINUTE);
        assertThat(isValid).isTrue();
    }

    @Test
    public void test_transactionValidator_invalidTransaction() {
        Transaction transaction = transactionTestUtils.createTransactionOutsideWindow(100, Constants.ONE_MINUTE);

        boolean isValid = transactionValidator.validateTransaction(transaction, Constants.ONE_MINUTE);
        assertThat(isValid).isFalse();
    }
}
