package com.n26.async;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.n26.common.Constants;
import com.n26.service.StatisticsService;

/**
 * Schedules the computation of the statistics to every duration of time
 * defined by a config property
 */
@Component
public class StatisticsScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsScheduler.class);

    @Inject
    private StatisticsService statisticsService;

    @Scheduled(fixedDelay = 1) // TODO: move to config property
    public void computeStatistics() {
        statisticsService.processLastWindow(Constants.ONE_MINUTE);
    }
}
