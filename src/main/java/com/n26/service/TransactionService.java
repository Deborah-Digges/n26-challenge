package com.n26.service;

import com.n26.entity.rest.Transaction;

/**
 * Performs operations on the transaction entity
 *
 * @author ddigges
 */
public interface TransactionService {

    /**
     * Creates a transaction in the system
     *
     * @param transaction
     * @return
     */
    Transaction createTransaction(Transaction transaction);
}
