package com.n26.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.OptionalDouble;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.n26.cache.StatisticsCache;
import com.n26.cache.TransactionCache;
import com.n26.entity.rest.Statistics;
import com.n26.entity.rest.Transaction;
import com.n26.mapper.StatisticsMapper;
import com.n26.service.StatisticsService;
import com.n26.validation.TransactionValidator;

/**
 * {@inheritDoc}
 *
 * @author ddigges
 */
@Component
public class StatisticsServiceImpl implements StatisticsService{
    @Inject
    private StatisticsCache statisticsCache;

    @Inject
    private StatisticsMapper statisticsMapper;

    @Inject
    private TransactionCache transactionCache;

    @Inject
    private TransactionValidator transactionValidator;


    /**
     * 1. Reads and clears transactions from the {@link TransactionCache#unprocessedTransactions} and adds them to
     * {@link TransactionCache#transactionsInWindow}
     *
     * 2. Removes transactions from {@link TransactionCache#transactionsInWindow} that are earlier than `windowSeconds`
     *
     * 3. Used transactions in {@link TransactionCache#transactionsInWindow} to compute statistics: sum, average, max, min, count
     *
     * 4. Updates statistics in {@link StatisticsCache}
     *
     * @param windowSeconds
     */
    @Override
    public void processLastWindow(int windowSeconds) {
        synchronized (transactionCache.getUnprocessedTransactions()) {
            transactionCache.getTransactionsInWindow().addAll(transactionCache.getUnprocessedTransactions());
            transactionCache.getUnprocessedTransactions().clear();
        }

        List<Transaction> transactionsToProcess = transactionCache.getTransactionsInWindow();
        removeIfNotInWindow(transactionsToProcess, windowSeconds);
        computeAndUpdateStatistics(transactionsToProcess);
    }

    @Override
    public Statistics getLatestStatistics() {
        return statisticsMapper.cacheToRest(statisticsCache);
    }

    /**
     * Update the statistics cache with the transactions in the current window
     * @param transactionsToProcess
     */
    private void computeAndUpdateStatistics(List<Transaction> transactionsToProcess) {
        if(transactionsToProcess.size() == 0) {
            updateStatisticsCache(null, null, null, null, 0L);
        } else {
            double sum = transactionsToProcess.stream().mapToDouble(t -> t.getAmount()).sum();
            double average = sum/transactionsToProcess.size();
            OptionalDouble max = transactionsToProcess.stream().mapToDouble(t->t.getAmount()).max();
            OptionalDouble min = transactionsToProcess.stream().mapToDouble(t->t.getAmount()).min();
            updateStatisticsCache(average, sum, max.getAsDouble(), min.getAsDouble(), transactionsToProcess.size());
        }
    }

    /**
     * Updates the statistics in the statistics cache
     * @param average
     * @param sum
     * @param max
     * @param min
     * @param count
     */
    private void updateStatisticsCache(Double average, Double sum, Double max, Double min, long count) {
        statisticsCache.setAverage(average);
        statisticsCache.setSum(sum);
        statisticsCache.setMax(max);
        statisticsCache.setMin(min);
        statisticsCache.setCount(count);
    }

    /**
     * Removes the transactions that are not in the window
     * @param transactionsToProcess
     * @param windowSeconds
     */
    private void removeIfNotInWindow(List<Transaction> transactionsToProcess, int windowSeconds) {
        synchronized (transactionsToProcess) {
            Iterator<Transaction> iterator = transactionsToProcess.listIterator();
            while(iterator.hasNext()){
                Transaction transaction = iterator.next();
                if(!transactionValidator.validateTransaction(transaction, windowSeconds)){
                    iterator.remove();
                }
            }
        }
    }
}
