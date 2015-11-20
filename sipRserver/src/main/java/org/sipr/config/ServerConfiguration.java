package org.sipr.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.sipr.SipRListener;
import org.sipr.core.config.sip.TcpListenerConfiguration;
import org.sipr.core.config.sip.TlsListenerConfiguration;
import org.sipr.core.config.sip.WsListenerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Configuration
@Import({ TcpListenerConfiguration.class,
        TlsListenerConfiguration.class,
        WsListenerConfiguration.class
})
public class ServerConfiguration {
    @Resource(name="tcpProviders")
    List<SipProvider> tcpProviders;

    @Resource(name="tlsProviders")
    List<SipProvider> tlsProviders;

    @Resource(name="wsProviders")
    List<SipProvider> wsProviders;

    @Resource(name="wssProviders")
    List<SipProvider> wssProviders;

    @Value("${service.registryAddress:localhost}")
    private String serviceRegistryAddress;

    @Value("${service.name}")
    private String serviceName;

    @Value("${sip.listener.tcp.port}")
    private Integer serviceTcpPort;

    @Value("${sip.listener.tls.port}")
    private Integer serviceTlsPort;

    @Value("${sip.listener.ws.port}")
    private Integer serviceWsPort;

    @Value("${sip.listener.wss.port}")
    private Integer serviceWssPort;

    @Value("${service.checkUrl}")
    private String serviceURLCheck;

    @Value("${service.checkInterval}")
    private String serviceCheckInterval;

    @Value("${service.address}")
    private String serviceAddress;

    @Bean
    public SipListener sipRServerListener() throws Exception {
        SipListener sipListener = new SipRListener();
        for (SipProvider tcpProvider : tcpProviders) {
            tcpProvider.addSipListener(sipListener);
        }

        for (SipProvider tlsProvider : tlsProviders) {
            tlsProvider.addSipListener(sipListener);
        }

        for (SipProvider wsProvider : wsProviders) {
            wsProvider.addSipListener(sipListener);
        }

        for (SipProvider wssProvider : wssProviders) {
            wssProvider.addSipListener(sipListener);
        }

        return sipListener;
    }

    @Bean
    public ConsulClient consulClient() {
        return new ConsulClient(serviceRegistryAddress);
    }

    @Bean
    public NewService serviceTcp() {
        NewService newService = new NewService();
        newService.setId("tcp-" + UUID.randomUUID().toString());
        newService.setName(serviceName);
        newService.setTags(Arrays.asList("tcp", "udp"));
        newService.setPort(serviceTcpPort);
        newService.setAddress(serviceAddress);
        return newService;
    }

    @Bean
    public NewService serviceTls() {
        NewService newService = new NewService();
        newService.setId("tls-" + UUID.randomUUID().toString());
        newService.setName(serviceName);
        newService.setTags(Collections.singletonList("tls"));
        newService.setPort(serviceTlsPort);
        newService.setAddress(serviceAddress);
        return newService;
    }

    @Bean
    public NewService serviceWs() {
        NewService newService = new NewService();
        newService.setId("ws-" + UUID.randomUUID().toString());
        newService.setName("ws");
        newService.setPort(serviceWsPort);
        newService.setAddress(serviceAddress);
        return newService;
    }

    @Bean
    public NewService serviceWss() {
        NewService newService = new NewService();
        newService.setId("wss-" + UUID.randomUUID().toString());
        newService.setName("wss");
        newService.setPort(serviceWssPort);
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
