package com.n26.entity.rest;

import java.io.Serializable;

/**
 * Encapsulates statistics about a transcation
 *
 * @author ddigges
 */
public class Statistics implements Serializable{
    private Double sum;
    private Double average;
    private Double max;
    private Double min;
    private long count;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
