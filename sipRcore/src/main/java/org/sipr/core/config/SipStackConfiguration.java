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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

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

    @Bean(name = "networkAddresses")
    public Set<String> networkAddresses() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        Set<String> addresses = new LinkedHashSet<>();
        while(nets.hasMoreElements())
        {
            NetworkInterface netInt = nets.nextElement();
            Enumeration<InetAddress> inetAdresses = netInt.getInetAddresses();
            while (inetAdresses.hasMoreElements())
            {
                InetAddress inetAddress = inetAdresses.nextElement();
                addresses.add(inetAddress.getHostAddress());
            }
        }
        return addresses;
    }

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

    @Bean(name = "tcpProviders")
    public List<SipProvider> tcpProviders() throws Exception {
        return createSipProviders(sipStack(), networkAddresses(), tcpPort, Arrays.asList("udp", "tcp"));
    }

    @Bean(name = "tlsProviders")
    public List<SipProvider> tlsProviders() throws Exception {
        return createSipProviders(sipStack(), networkAddresses(), tlsPort, Collections.singletonList("tls"));
    }

    @Bean(name = "wsProviders")
    public List<SipProvider> wsProviders() throws Exception {
        return createSipProviders(sipStack(), networkAddresses(), wsPort, Collections.singletonList("ws"));
    }

    @Bean(name = "wssProviders")
    public List<SipProvider> wssProviders() throws Exception {
        return createSipProviders(sipStack(), networkAddresses(), wssPort, Collections.singletonList("wss"));
    }

    private List<SipProvider> createSipProviders(SipStack sipStack, Set<String> addresses, int port, List<String> schemas) throws Exception {
        List<SipProvider> providers = new LinkedList<>();
        for (String address : addresses) {
            SipProvider provider = null;
            for (String schema : schemas) {
                ListeningPoint listeningPoint = sipStack.createListeningPoint(address, port, schema);
                if (provider == null) {
                    provider = sipStack().createSipProvider(listeningPoint);
                } else {
                    provider.addListeningPoint(listeningPoint);
                }
            }
            providers.add(provider);
        }
        return providers;
    }

    @Bean(name = "sipUtils")
    public SipUtils sipUtils() throws Exception {
        return new SipUtils();
    }

}
