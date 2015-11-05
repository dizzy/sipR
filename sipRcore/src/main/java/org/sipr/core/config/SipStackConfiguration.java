package org.sipr.core.config;

import gov.nist.javax.sip.message.MessageFactoryImpl;
import org.sipr.core.utils.SipUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.sip.ListeningPoint;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ServerHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.message.MessageFactory;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class SipStackConfiguration {
    @Value("${sip.agent.header}")
    private String agentHeader;

    @Value("${sip.listener.tcp.port}")
    private int tcpPort;

    @Value("${sip.listener.tls.port}")
    private int tlsPort;

    @Value("${sip.listener.ws.port}")
    private int wsPort;

    @Value("${sip.listener.wss.port}")
    private int wssPort;

    @Value("${sip.listener.address}")
    private String listenerAddress;

    @Bean(name = "sipProperties")
    public Properties sipProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource("/sip-protocol.properties"));
    }

    @Bean(name = "sipFactory")
    public SipFactory sipFactory() {
        SipFactory sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        return sipFactory;
    }

    @Bean(name = "headerFactory")
    public HeaderFactory headerFactory() throws Exception {
        return sipFactory().createHeaderFactory();
    }

    @Bean(name = "userAgentHeader")
    public UserAgentHeader userAgentHeader() throws Exception {
        return (UserAgentHeader) headerFactory().createHeader(UserAgentHeader.NAME, agentHeader);
    }

    @Bean(name = "serverHeader")
    public ServerHeader serverHeader() throws Exception {
        return (ServerHeader) headerFactory().createHeader(ServerHeader.NAME, agentHeader);
    }

    @Bean(name = "addressFactory")
    public AddressFactory addressFactory() throws Exception {
        return sipFactory().createAddressFactory();
    }

    @Bean(name = "messageFactory")
    public MessageFactory messageFactory() throws Exception {
        MessageFactory factory = sipFactory().createMessageFactory();
        ((MessageFactoryImpl) factory).setDefaultUserAgentHeader(userAgentHeader());
        ((MessageFactoryImpl) factory).setDefaultServerHeader(serverHeader());
        return factory;
    }

    @Bean(name = "sipStack", initMethod = "start", destroyMethod = "stop")
    public SipStack sipStack() throws Exception {
        return sipFactory().createSipStack(sipProperties());
    }

    @Bean
    public ListeningPoint udpListeningPoint() throws Exception {
        return sipStack().createListeningPoint(listenerAddress, tcpPort, "udp");
    }

    @Bean
    public ListeningPoint tcpListeningPoint() throws Exception {
        return sipStack().createListeningPoint(listenerAddress, tcpPort, "tcp");
    }

    @Bean
    public ListeningPoint tlsListeningPoint() throws Exception {
        return sipStack().createListeningPoint(listenerAddress, tlsPort, "tls");
    }

    @Bean
    public ListeningPoint wsListeningPoint() throws Exception {
        return sipStack().createListeningPoint(listenerAddress, wsPort, "ws");
    }

    @Bean
    public ListeningPoint wssListeningPoint() throws Exception {
        return sipStack().createListeningPoint(listenerAddress, wssPort, "wss");
    }

    @Bean(name = "tcpProvider")
    public SipProvider tcpProvider() throws Exception {
        SipProvider tcpProvider = sipStack().createSipProvider(udpListeningPoint());
        tcpProvider.addListeningPoint(tcpListeningPoint());
        return tcpProvider;
    }

    @Bean(name = "tlsProvider")
    public SipProvider tlsProvider() throws Exception {
        return sipStack().createSipProvider(tlsListeningPoint());
    }

    @Bean(name = "wsProvider")
    public SipProvider wsProvider() throws Exception {
        return sipStack().createSipProvider(wsListeningPoint());
    }

    @Bean(name = "wssProvider")
    public SipProvider wssProvider() throws Exception {
        return sipStack().createSipProvider(wssListeningPoint());
    }

    @Bean(name = "sipUtils")
    public SipUtils sipUtils() throws Exception {
        return new SipUtils();
    }

}
