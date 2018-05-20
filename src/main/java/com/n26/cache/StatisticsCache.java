package com.n26.cache;

import javax.inject.Singleton;

import org.springframework.stereotype.Component;

/**
 * Will serve as an in-memory cache for the
 * pre-computed statistics for the configured window
 *
 * @author ddigges
 */
@Singleton
@Component
public class StatisticsCache {

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
