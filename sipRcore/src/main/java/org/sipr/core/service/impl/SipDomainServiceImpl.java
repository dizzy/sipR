package org.sipr.core.service.impl;

import org.sipr.core.service.SipDomainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SipDomainServiceImpl implements SipDomainService {

    @Value("#{'${sip.domains}'.split(',')}")
    private List<String> sipDomain;

    @Override
    public boolean isValidDomain(String domain) {
        return sipDomain.contains(domain);
    }
}
