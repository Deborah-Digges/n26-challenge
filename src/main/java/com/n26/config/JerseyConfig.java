package com.n26.config;

import com.n26.resource.StatisticsResource;
import com.n26.resource.TransactionResource;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Jersey configuration for the spring boot application.
 *
 * @author ddigges
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(StatisticsResource.class);
        register(TransactionResource.class);
    }
}