package com.n26.mapper;

import org.mapstruct.Mapper;

import com.n26.cache.StatisticsCache;
import com.n26.entity.rest.Statistics;

/**
 * Maps between the classes {@link com.n26.cache.StatisticsCache}
 * and {@link com.n26.entity.rest.Statistics}
 */
@Mapper
public interface StatisticsMapper{

    /**
     * Maps from the cache representation to the REST representation
     * @param statisticsCache
     * @return
     */
    Statistics cacheToRest(StatisticsCache statisticsCache);
}
