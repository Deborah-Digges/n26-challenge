package com.n26.resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.n26.entity.rest.Statistics;
import com.n26.service.StatisticsService;

import static com.n26.common.Constants.STATISTICS_BASE_URI;

/**
 * Resource class for the *StatisticsResource* entity that
 * permits the read operation
 *
 * @author ddigges
 */
@Singleton
@Path(STATISTICS_BASE_URI)
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StatisticsResource {
    @Inject
    private StatisticsService statisticsService;

    @GET
    public Statistics getStatistics() {
        return statisticsService.getLatestStatistics();
    }
}
