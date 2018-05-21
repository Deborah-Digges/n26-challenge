package com.n26.service;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.testing.threadtester.AnnotatedTestRunner;
import com.google.testing.threadtester.ThreadedAfter;
import com.google.testing.threadtester.ThreadedBefore;
import com.google.testing.threadtester.ThreadedMain;
import com.google.testing.threadtester.ThreadedSecondary;
import com.n26.cache.StatisticsCache;
import com.n26.cache.TransactionCache;
import com.n26.common.Constants;
import com.n26.common.TransactionTestUtils;
import com.n26.entity.rest.Transaction;
import com.n26.mapper.StatisticsMapper;
import com.n26.mapper.StatisticsMapperImpl;
import com.n26.service.impl.StatisticsServiceImpl;
import com.n26.service.impl.TransactionServiceImpl;
import com.n26.validation.TransactionValidator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Verifies that if two statistics threads are being run in parallel
 * along with creation of transactions, the results are deterministic
 *
 * @author ddigges
 */
public class StatisticsConcurrencyTest {
    private StatisticsService statisticsService = new StatisticsServiceImpl();
    private StatisticsCache statisticsCache = new StatisticsCache();
    private StatisticsMapper statisticsMapper = new StatisticsMapperImpl();
    private TransactionCache transactionCache = new TransactionCache();
    private TransactionValidator transactionValidator = new TransactionValidator();

    private TransactionService transactionService = new TransactionServiceImpl();
    private TransactionTestUtils transactionTestUtils = new TransactionTestUtils();

    @Test(expected = RuntimeException.class)
    public void testConcurrentTransactionCreation() {
        AnnotatedTestRunner runner = new AnnotatedTestRunner();
        runner.setDebug(true);
        runner.runTests(this.getClass(), TransactionCreationConcurrencyTest.class);
    }

    @ThreadedBefore
    public void before() {
        // Setup dependencies for statisticsService
        ReflectionTestUtils.setField(statisticsService, "statisticsCache", statisticsCache);
        ReflectionTestUtils.setField(statisticsService, "statisticsMapper", statisticsMapper);
        ReflectionTestUtils.setField(statisticsService, "transactionCache", transactionCache);
        ReflectionTestUtils.setField(statisticsService, "transactionValidator", transactionValidator);

        // Setup dependencies for transactionService
        ReflectionTestUtils.setField(transactionService, "transactionCache", transactionCache);

    }

    @ThreadedMain
    public void main() {
        // Create a new transaction
        Transaction transaction = transactionTestUtils.createTransactionInWindow(200);
        transactionService.createTransaction(transaction);

        statisticsService.processLastWindow(Constants.ONE_MINUTE);
    }

    @ThreadedSecondary
    public void secondary() {
        // Create a new transaction
        Transaction transaction = transactionTestUtils.createTransactionInWindow(100);
        transactionService.createTransaction(transaction);

        statisticsService.processLastWindow(Constants.ONE_MINUTE);
    }

    @ThreadedAfter
    public void after() {
        assertThat(transactionCache.getUnprocessedTransactions()).isEqualTo(0);
        assertThat(transactionCache.getTransactionsInWindow()).isEqualTo(2);
        assertThat(statisticsCache.getAverage()).isEqualTo(150);
        assertThat(statisticsCache.getCount()).isEqualTo(2);
        assertThat(statisticsCache.getMax()).isEqualTo(200);
        assertThat(statisticsCache.getMin()).isEqualTo(100);
        assertThat(statisticsCache.getSum()).isEqualTo(300);
    }
}
