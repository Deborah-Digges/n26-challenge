package com.n26.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.n26.cache.TransactionCache;
import com.n26.common.Constants;
import com.n26.entity.rest.Transaction;
import com.n26.service.impl.TransactionServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ddigges
 */
@RunWith(SpringRunner.class)
public class TransactionServiceTest {
    private TransactionService transactionService = new TransactionServiceImpl();
    private TransactionCache transactionCache = new TransactionCache();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(transactionService, "transactionCache", transactionCache);
    }

    @Test
    public void test_createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(System.currentTimeMillis()/ Constants.MILLI_SECONDS);
        transaction.setAmount(1000);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        assertThat(createdTransaction).isNotNull();
        assertThat(createdTransaction.getId()).isNotNull();
        assertThat(createdTransaction.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(createdTransaction.getTimestamp()).isEqualTo(transaction.getTimestamp());
        assertThat(transactionCache.getUnprocessedTransactions().size()).isEqualTo(1);
    }
}
