package com.n26.common;

import com.n26.entity.rest.Transaction;

/**
 * Creates transactions with different states
 * @author ddigges
 */
public class TransactionTestUtils {

    /**
     * Creates a transaction outside the statistics computation window
     * @param amount
     * @param windowSeconds
     * @return
     */
    public Transaction createTransactionOutsideWindow(double amount, int windowSeconds) {
        return createTransaction(amount, System.currentTimeMillis()/Constants.MILLI_SECONDS - (windowSeconds + 1));
    }

    /**
     * Creates a transaction within the statistics compuitation window
     * @param amount
     * @return
     */
    public Transaction createTransactionInWindow(double amount) {
        return createTransaction(amount, System.currentTimeMillis()/Constants.MILLI_SECONDS);
    }

    /**
     * Creates a transaction with the given amount and timestamp
     * @param amount
     * @param timestamp
     * @return
     */
    public Transaction createTransaction(double amount, long timestamp) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimestamp(timestamp);
        return transaction;
    }
}
