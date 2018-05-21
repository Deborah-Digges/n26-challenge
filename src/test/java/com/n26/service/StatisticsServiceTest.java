package com.n26.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.n26.cache.StatisticsCache;
import com.n26.cache.TransactionCache;
import com.n26.common.Constants;
import com.n26.common.TransactionTestUtils;
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
    private TransactionTestUtils transactionTestUtils = new TransactionTestUtils();

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
    public void test_Workflow1() {
        // creates three transactions, one of which is more than one minute ago
        Transaction transaction1 = transactionTestUtils.createTransactionInWindow(200);
        Transaction transaction2 = transactionTestUtils.createTransactionOutsideWindow(150, Constants.ONE_MINUTE);
        Transaction transaction3 = transactionTestUtils.createTransactionInWindow(100);
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);
        transactionService.createTransaction(transaction3);

        // Manually trigger the process statistics flow
        statisticsService.processLastWindow(Constants.ONE_MINUTE);

        assertStatistics(2, 150, 200, 100);
    }


    @Test
    public void test_Workflow2() {
        // creates three transactions, one of which is more than one minute ago
        Transaction transaction1 = transactionTestUtils.createTransactionInWindow(200);
        transactionService.createTransaction(transaction1);

        Transaction transaction2 = transactionTestUtils.createTransactionOutsideWindow(150, Constants.ONE_MINUTE);
        transactionService.createTransaction(transaction2);

        Transaction transaction3 = transactionTestUtils.createTransactionInWindow(100);
        transactionService.createTransaction(transaction3);

        // Manually trigger the process statistics flow
        statisticsService.processLastWindow(Constants.ONE_MINUTE);

        assertStatistics(2, 150, 200, 100);

        Transaction transaction4 = transactionTestUtils.createTransactionInWindow(50);
        transactionService.createTransaction(transaction4);

        // Manually trigger the process statistics flow
        statisticsService.processLastWindow(Constants.ONE_MINUTE);

        assertStatistics(3, 350.0 / 3, 200, 50);


    }

    private void assertStatistics(int count, double average, int max, int min) {
        assertThat(transactionCache.getUnprocessedTransactions().size()).isEqualTo(0);
        assertThat(transactionCache.getTransactionsInWindow().size()).isEqualTo(count);

        Statistics statistics2 = statisticsService.getLatestStatistics();
        assertThat(statistics2.getCount()).isEqualTo(count);
        assertThat(statistics2.getAverage()).isEqualTo(average);
        assertThat(statistics2.getMax()).isEqualTo(max);
        assertThat(statistics2.getMin()).isEqualTo(min);
    }
}
