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
import com.n26.common.TransactionTestUtils;
import com.n26.entity.rest.Transaction;
import com.n26.mapper.StatisticsMapper;
import com.n26.mapper.StatisticsMapperImpl;
import com.n26.service.impl.StatisticsServiceImpl;
import com.n26.service.impl.TransactionServiceImpl;
import com.n26.validation.TransactionValidator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Verifies concurrent transaction creation
 * 
 * @author ddigges
 */
public class TransactionCreationConcurrencyTest {
    private StatisticsService statisticsService = new StatisticsServiceImpl();
    private StatisticsCache statisticsCache = new StatisticsCache();
    private StatisticsMapper statisticsMapper = new StatisticsMapperImpl();
    private TransactionCache transactionCache = new TransactionCache();
    private TransactionValidator transactionValidator = new TransactionValidator();

    private TransactionService transactionService = new TransactionServiceImpl();
    private TransactionTestUtils transactionTestUtils = new TransactionTestUtils();

    @Test(expected = RuntimeException.class)
    public void testConcurrentTransactionCreation() {
        // Create an AnnotatedTestRunner that will run the threaded tests defined in this
        // class.
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
    }

    @ThreadedSecondary
    public void secondary() {
        // Create a new transaction
        Transaction transaction = transactionTestUtils.createTransactionInWindow(200);
        transactionService.createTransaction(transaction);
    }

    @ThreadedAfter
    public void after() {
        // The transaction cache should contain 2 transactions
        assertThat(transactionCache.getUnprocessedTransactions()).isEqualTo(2);
    }
}
