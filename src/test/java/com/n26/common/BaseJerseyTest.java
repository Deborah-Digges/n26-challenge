package com.n26.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.n26.config.ApplicationConfig;
import com.n26.config.MockCommonBeanDef;

public abstract class BaseJerseyTest extends JerseyTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseJerseyTest.class);

    private ApplicationContext context;


    @Override
    protected Application configure() {

        final ResourceConfig rc = new ResourceConfig();

        this.context = new AnnotationConfigApplicationContext(MockCommonBeanDef.class);
        rc.property("contextConfig", this.context);
        // Find first available port.
        forceSet(TestProperties.CONTAINER_PORT, "0");
        rc.register(TestProperties.LOG_TRAFFIC);
        rc.register(SpringLifecycleListener.class);
        rc.register(ContextLoaderListener.class);
        rc.register(RequestContextFilter.class);
        rc.register(RequestContextListener.class);

        return rc;
    }

    public ApplicationContext getContext() {
        return this.context;
    }
}