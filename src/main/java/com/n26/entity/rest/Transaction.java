package com.n26.entity.rest;

import java.io.Serializable;

/**
 * Encapsulates data for a single transaction
 *
 * @author ddigges
 */
public class Transaction implements Serializable{

    /**
     * Unique identifier for the transaction
     */
    private Long id;

    /**
     * The transaction amount
     */
    private double amount;

    /**
     * The transaction time in epoch in millis in UTC time zone
     */
    private Long timestamp;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
