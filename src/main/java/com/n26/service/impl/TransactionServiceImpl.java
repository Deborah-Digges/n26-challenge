package com.n26.service.impl;

import com.n26.entity.rest.Transaction;
import com.n26.service.TransactionService;

import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 */
@Component
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(1L);
        return transaction;
    }
}
