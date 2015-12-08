package org.sipr.core.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SipDomainServiceImplTest {

    @Test
    public void testIsValidDomain() throws Exception {
        List<String> domains = new ArrayList<>();
        domains.add("domain1");
        domains.add("domain2");

        Set<String> networkAddresses = new HashSet<>();
        networkAddresses.add("192.168.0.1");
        networkAddresses.add("2001:db8:0:1:1:1:1:1");

        SipDomainServiceImpl service = new SipDomainServiceImpl();
        service.networkAddresses = networkAddresses;
        service.sipDomain = domains;

        service.init();

        assertTrue(service.isValidDomain("domain1"));
        assertTrue(service.isValidDomain("domain2"));
        assertTrue(service.isValidDomain("192.168.0.1"));
        assertTrue(service.isValidDomain("2001:db8:0:1:1:1:1:1"));
        assertFalse(service.isValidDomain("192.168.0.11"));
        assertFalse(service.isValidDomain("bogus"));
    }
}