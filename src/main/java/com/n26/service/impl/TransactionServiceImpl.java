package com.n26.service.impl;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.n26.async.StatisticsScheduler;
import com.n26.cache.TransactionCache;
import com.n26.entity.rest.Transaction;
import com.n26.service.TransactionService;

/**
 * {@inheritDoc}
 */
@Component
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsScheduler.class);

    @Inject
    private TransactionCache transactionCache;

    /**
     * Adds the transaction to the list of unprocessed transactions which will be
     * picked up for statistics
     * @param transaction
     * @return
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        LOG.info("[Started] method=createTransaction");
        transaction.setId(UUID.randomUUID().getMostSignificantBits());
        transactionCache.getUnprocessedTransactions().add(transaction);
        LOG.info("[Ended] method=createTransaction");
        return transaction;

    }
}
