package org.sipr.core.config.sip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
public class TcpListenerConfiguration extends BaseListenerConfiguration {

    @Value("${sip.listener.tcp.port}")
    private int tcpPort;

    @Resource(name = "networkAddresses")
    Set<String> networkAddresses;

    @Resource(name = "sipStack")
    SipStack sipStack;

    @Bean(name = "tcpProviders")
    public List<SipProvider> tcpProviders() throws Exception {
        return createSipProviders(sipStack, networkAddresses, tcpPort, Arrays.asList("udp", "tcp"));
    }

}
