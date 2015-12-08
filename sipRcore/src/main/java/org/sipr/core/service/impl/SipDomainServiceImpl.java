package org.sipr.core.service.impl;

import org.sipr.core.service.SipDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SipDomainServiceImpl implements SipDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipDomainServiceImpl.class);

    @Value("#{'${sip.domains}'.split(',')}")
    List<String> sipDomain;

    @Resource(name = "networkAddresses")
    Set<String> networkAddresses;

    Set<String> normalizedAddresses = new HashSet<>();

    @PostConstruct
    void init() throws UnknownHostException {
        for (String networkAddress : networkAddresses) {
            normalizedAddresses.add(InetAddress.getByName(networkAddress).toString());
        }
    }

    @Override
    public boolean isValidDomain(String domain) {
        if (sipDomain.contains(domain)) {
            return true;
        }

        try {
            String address = InetAddress.getByName(domain).toString();
            for (String normalizedAddress : normalizedAddresses) {
                if (normalizedAddress.contains(address)) {
                    return true;
                }
            }
        } catch (UnknownHostException ex) {
            return false;
        }

        return false;
    }
}
