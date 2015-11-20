package org.sipr.core.config.sip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WsListenerConfiguration extends BaseListenerConfiguration {

    @Value("${sip.listener.ws.port}")
    private int wsPort;

    @Value("${sip.listener.wss.port}")
    private int wssPort;

    @Resource(name = "networkAddresses")
    Set<String> networkAddresses;

    @Resource(name = "sipStack")
    SipStack sipStack;

    @Bean(name = "wsProviders")
    public List<SipProvider> wsProviders() throws Exception {
        return createSipProviders(sipStack, networkAddresses, wsPort, Collections.singletonList("ws"));
    }

    @Bean(name = "wssProviders")
    public List<SipProvider> wssProviders() throws Exception {
        return createSipProviders(sipStack, networkAddresses, wssPort, Collections.singletonList("wss"));
    }
}
