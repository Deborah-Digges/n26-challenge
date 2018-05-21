package com.n26.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.n26.cache.StatisticsCache;
import com.n26.cache.TransactionCache;
import com.n26.common.Constants;
import com.n26.entity.rest.Statistics;
import com.n26.entity.rest.Transaction;
import com.n26.mapper.StatisticsMapper;
import com.n26.mapper.StatisticsMapperImpl;
import com.n26.service.impl.StatisticsServiceImpl;
import com.n26.service.impl.TransactionServiceImpl;
import com.n26.validation.TransactionValidator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ddigges
 */
@RunWith(SpringRunner.class)
public class StatisticsServiceTest {
    private StatisticsService statisticsService = new StatisticsServiceImpl();
    private StatisticsCache statisticsCache = new StatisticsCache();
    private StatisticsMapper statisticsMapper = new StatisticsMapperImpl();
    private TransactionCache transactionCache = new TransactionCache();
    private TransactionValidator transactionValidator = new TransactionValidator();

    private TransactionService transactionService = new TransactionServiceImpl();

    @Before
    public void setup() {
        // Setup dependencies for statisticsService
        ReflectionTestUtils.setField(statisticsService, "statisticsCache", statisticsCache);
        ReflectionTestUtils.setField(statisticsService, "statisticsMapper", statisticsMapper);
        ReflectionTestUtils.setField(statisticsService, "transactionCache", transactionCache);
        ReflectionTestUtils.setField(statisticsService, "transactionValidator", transactionValidator);

        // Setup dependencies for transactionService
        ReflectionTestUtils.setField(transactionService, "transactionCache", transactionCache);

    }

    /**
     * Creates three transactions: two are within the 60 second window.
     * Verifies the statistics for the last window
     */
    @Test
    public void test_Workflow() {
        // creates three transactions, one of which is more than one minute ago
        Transaction transaction1 = createTransaction(200, System.currentTimeMillis()/ Constants.MILLI_SECONDS);
        Transaction transaction2 = createTransaction(150, System.currentTimeMillis()/ Constants.MILLI_SECONDS - (Constants.ONE_MINUTE + 1));
        Transaction transaction3 = createTransaction(100, System.currentTimeMillis()/ Constants.MILLI_SECONDS);

        // Manually trigger the process statistics flow
        statisticsService.processLastWindow(Constants.ONE_MINUTE);

        assertThat(transactionCache.getUnprocessedTransactions().size()).isEqualTo(0);
        assertThat(transactionCache.getTransactionsInWindow().size()).isEqualTo(2);

        Statistics statistics = statisticsService.getLatestStatistics();
        assertThat(statistics.getCount()).isEqualTo(2);
        assertThat(statistics.getAverage()).isEqualTo(150);
        assertThat(statistics.getMax()).isEqualTo(200);
        assertThat(statistics.getMin()).isEqualTo(100);
    }

    private Transaction createTransaction(double amount, long timestamp) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimestamp(timestamp);
        return transactionService.createTransaction(transaction);
    }
}
