package org.sipr.service;

import com.ecwid.consul.transport.TransportException;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.List;

@Component
public class ServiceRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistrar.class);

    @Inject
    List<NewService> services;

    @Inject
    NewService.Check serviceCheck;

    @Inject
    ConsulClient consulClient;

    @PostConstruct
    void init() {
        try {
            for (NewService service : services) {
                service.setCheck(serviceCheck);
                consulClient.agentServiceRegister(service);
            }
        } catch (TransportException tex) {
            LOGGER.error("Could not register service ");
        }
    }

    @PreDestroy
    void destroy() {
        for (NewService service : services) {
            consulClient.agentServiceDeregister(service.getId());
        }
    }
}
