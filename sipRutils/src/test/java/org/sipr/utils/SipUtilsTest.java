package org.sipr.utils;

import gov.nist.javax.sip.address.SipUri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SipUtilsTest {

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    AddressFactory addressFactory;

    @Mock
    HeaderFactory headerFactory;

    @Mock
    CallIdHeader callIdHeader;

    @Mock
    URI uri;

    @Mock
    ToHeader toHeader;

    @Mock
    Address toAddress;

    SipUtilsImpl sipUtils;

    @Before
    public void setUp() throws Exception {
        sipUtils = new SipUtilsImpl();
        sipUtils.addressFactory = addressFactory;
        sipUtils.headerFactory = headerFactory;

        when(requestEvent.getRequest()).thenReturn(request);
    }

    @Test
    public void testGetCallId() throws Exception {
        when(request.getHeader(CallIdHeader.NAME)).thenReturn(callIdHeader);
        when(callIdHeader.getCallId()).thenReturn("12dsft43tgfgdfg");
        assertEquals("12dsft43tgfgdfg", sipUtils.getCallId(requestEvent));
    }

    @Test
    public void testGetRequestMethod() throws Exception {
        when(request.getMethod()).thenReturn("METHOD");
        assertEquals("METHOD", sipUtils.getRequestMethod(requestEvent));
    }

    @Test
    public void testGetCanonicalizedURI() throws Exception {
        SipUri sipUri = new SipUri();
        sipUri.setGrParam("");
        sipUri.setLrParam();
        sipUri.setHost("localhost");
        sipUri.setPort(5060);
        assertEquals("sip:localhost:5060", sipUtils.getCanonicalizedURI(sipUri).toString());

        when(uri.isSipURI()).thenReturn(false);
        assertEquals(uri, sipUtils.getCanonicalizedURI(uri));

        assertNull(sipUtils.getCanonicalizedURI(null));
    }

}