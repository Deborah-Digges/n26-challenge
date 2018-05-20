package com.n26.service;

import com.n26.entity.rest.Statistics;

/**
 * Performs operations on the statistics com.n26.resource
 *
 * @author ddigges
 */
public interface StatisticsService {
    /**
     * Computes statistics for the last `windowSeconds`
     * seconds and updates it in {@link com.n26.cache.StatisticsCache}
     */
    void processLastWindow(int windowSeconds);

    Statistics getLatestStatistics();
}
