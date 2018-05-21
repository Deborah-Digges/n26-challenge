package com.n26.resource;

import java.util.UUID;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.n26.common.BaseJerseyTest;
import com.n26.common.Constants;
import com.n26.entity.rest.Transaction;
import com.n26.service.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * @author ddigges JerseyTest
 */
public class TransactionResourceTest extends BaseJerseyTest{

    @Test
    public void test_createTransaction_OlderThanOneMinute() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(System.currentTimeMillis()/ Constants.MILLI_SECONDS - (Constants.ONE_MINUTE + 1));
        transaction.setAmount(100);

        TransactionResource transactionResource = getContext().getBean(TransactionResource.class);
        Response response = transactionResource.createTransaction(transaction);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void test_createTransaction_WithinWindow() {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(System.currentTimeMillis()/ Constants.MILLI_SECONDS);
        transaction.setAmount(100);

        TransactionService transactionService = getContext().getBean(TransactionService.class);
        doReturn(setIdOntransaction(transaction)).when(transactionService).createTransaction(any(Transaction.class));

        TransactionResource transactionResource = getContext().getBean(TransactionResource.class);
        Response response = transactionResource.createTransaction(transaction);

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
    }

    public Transaction setIdOntransaction(Transaction transaction) {
        transaction.setId(UUID.randomUUID().getMostSignificantBits());
        return transaction;
    }
}
