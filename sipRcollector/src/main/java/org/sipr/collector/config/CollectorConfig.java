package org.sipr.collector.config;

import org.sipr.collector.SipRListener;
import org.sipr.core.config.sip.SipStackConfiguration;
import org.sipr.core.config.sip.TcpListenerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import java.util.List;

@Configuration
@Import( { SipStackConfiguration.class, TcpListenerConfiguration.class} )
public class CollectorConfig {

    @Resource(name="tcpProviders")
    List<SipProvider> tcpProviders;

    @Bean
    public SipListener sipRServerListener() throws Exception {
        SipListener sipListener = new SipRListener();
        for (SipProvider tcpProvider : tcpProviders) {
            tcpProvider.addSipListener(sipListener);
        }

        return sipListener;
    }

}
