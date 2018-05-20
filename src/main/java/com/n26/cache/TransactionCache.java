package com.n26.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import org.springframework.stereotype.Component;

import com.n26.entity.rest.Transaction;

/**
 * Will serve as an in memory cache for transactions
 *
 * @author ddigges
 */
@Singleton
@Component
public class TransactionCache {
    /**
     * Represents the transactions which haven't yet been processed
     * and added to the rolling statistics
     */
    private List<Transaction> unprocessedTransactions = Collections.synchronizedList(new ArrayList<Transaction>());

    /**
     * Represents transactions in the current window for which
     * statistics are being generated
     */
    private List<Transaction> transactionsInWindow = Collections.synchronizedList(new ArrayList<Transaction>());

    public List<Transaction> getUnprocessedTransactions() {
        return unprocessedTransactions;
    }

    public void setUnprocessedTransactions(List<Transaction> unprocessedTransactions) {
        this.unprocessedTransactions = unprocessedTransactions;
    }

    public List<Transaction> getTransactionsInWindow() {
        return transactionsInWindow;
    }

    public void setTransactionsInWindow(List<Transaction> transactionsInWindow) {
        this.transactionsInWindow = transactionsInWindow;
    }
}
