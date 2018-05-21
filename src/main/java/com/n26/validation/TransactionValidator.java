package com.n26.validation;

import com.n26.common.Constants;
import com.n26.entity.rest.Transaction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

/**
 * Performs com.n26.validation on the Transaction REST entity
 *
 * @author ddigges
 */
@Component
public class TransactionValidator {

    /**
     * Performs the following com.n26.validation on transaction:
     *
     * if the time-stamp is older than a minute, return as invalid
     * @param transaction:
     * @param window: the duration of time within which transactions are allowed
     * @return
     */
    public boolean validateTransaction(Transaction transaction, int window) {
        Long currentTimestamp = System.currentTimeMillis();
        if(currentTimestamp - transaction.getTimestamp() > window * Constants.MILLI_SECONDS) {
            return false;
        }
        return true;
    }
}
