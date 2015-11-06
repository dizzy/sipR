package org.sipr.core.service.impl;

import org.sipr.core.service.SipDomainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class SipDomainServiceImpl implements SipDomainService {

    @Value("#{'${sip.domains}'.split(',')}")
    private List<String> sipDomain;

    @Resource(name = "networkAddresses")
    Set<String> networkAddresses;

    @Override
    public boolean isValidDomain(String domain) {
        return sipDomain.contains(domain) || networkAddresses.contains(domain);
    }
}
