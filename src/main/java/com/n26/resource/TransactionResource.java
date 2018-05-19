package com.n26.resource;

import com.n26.entity.rest.Transaction;
import com.n26.service.TransactionService;
import com.n26.validation.TransactionValidator;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static com.n26.common.Constants.PATH_SEPARATOR;
import static com.n26.common.Constants.TRANSACTIONS_BASE_URI;

/**
 *  Resource class for the *TransactionResource* entity that permits CRUD operations
 *
 *  @author ddigges
 */
@Singleton
@Path(TRANSACTIONS_BASE_URI)
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TransactionResource {
    @Inject
    private TransactionValidator transactionValidator;

    @Inject
    private TransactionService transactionService;

    @POST
    public Response createTransaction(Transaction transaction) {
        if(!transactionValidator.validateTransaction(transaction)) {
            return Response.noContent().build();
        }
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        URI resourceURI = UriBuilder.fromPath(TRANSACTIONS_BASE_URI.concat(PATH_SEPARATOR).concat(createdTransaction.getId().toString())).build();
        return Response.created(resourceURI).entity(transaction).build();
    }

}
