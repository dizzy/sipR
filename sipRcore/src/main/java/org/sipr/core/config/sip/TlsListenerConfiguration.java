package org.sipr.core.config.sip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Configuration
public class TlsListenerConfiguration extends BaseListenerConfiguration {

    @Value("${sip.listener.tls.port}")
    private int tlsPort;

    @Resource(name = "networkAddresses")
    Set<String> networkAddresses;

    @Resource(name = "sipStack")
    SipStack sipStack;

    @Bean(name = "tlsProviders")
    public List<SipProvider> tlsProviders() throws Exception {
        return createSipProviders(sipStack, networkAddresses, tlsPort, Collections.singletonList("tls"));
    }


}
