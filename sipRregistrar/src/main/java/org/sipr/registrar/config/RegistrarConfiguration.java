package org.sipr.registrar.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.sipr.registrar.request.RequestDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import java.util.Arrays;

@Configuration
public class RegistrarConfiguration {
    @Inject
    SipProvider tcpProvider;

    @Inject
    SipProvider tlsProvider;

    @Inject
    SipProvider wsProvider;

    @Inject
    SipProvider wssProvider;

    @Value("${service.registryAddress:localhost}")
    private String serviceRegistryAddress;

    @Value("${service.name}")
    private String serviceName;

    @Value("${sip.listener.tcp.port}")
    private Integer serviceTcpPort;

    @Value("${sip.listener.tls.port}")
    private Integer serviceTlsPort;

    @Value("${service.checkUrl}")
    private String serviceURLCheck;

    @Value("${service.checkInterval}")
    private String serviceCheckInterval;

    @Value("${service.address}")
    private String serviceAddress;

    @Bean
    public SipListener sipRRegistrarListener() throws Exception {
        SipListener sipListener = new RequestDispatcher();
        tcpProvider.addSipListener(sipListener);
        tlsProvider.addSipListener(sipListener);
        wsProvider.addSipListener(sipListener);
        wssProvider.addSipListener(sipListener);
        return sipListener;
    }

    @Bean
    public ConsulClient consulClient() {
        return new ConsulClient(serviceRegistryAddress);
    }

    @Bean
    public NewService serviceTcp() {
        NewService newService = new NewService();
        newService.setId("george1");
        newService.setName(serviceName);
        newService.setTags(Arrays.asList("tcp", "udp"));
        newService.setPort(serviceTcpPort);
        newService.setAddress(serviceAddress);
        return newService;
    }

    @Bean
    public NewService serviceTls() {
        NewService newService = new NewService();
        newService.setId("george2");
        newService.setName(serviceName);
        newService.setTags(Arrays.asList("tls"));
        newService.setPort(serviceTlsPort);
        newService.setAddress(serviceAddress);
        return newService;
    }

    @Bean
    public NewService.Check serviceCheck() {
        NewService.Check serviceCheck = new NewService.Check();
        serviceCheck.setHttp(serviceURLCheck);
        serviceCheck.setInterval(serviceCheckInterval);
        return serviceCheck;
    }

}
