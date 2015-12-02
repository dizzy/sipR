package org.sipr.request.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.utils.SipUtils;

import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ToHeaderValidatorTest {

    @Mock
    SipUtils sipUtils;

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    ToHeader toHeader;

    @Mock
    Address toAddress;

    @Mock
    SipURI toAddressURI;

    @Mock
    SipURI requestURI;

    @Before
    public void setUp() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
        when(request.getHeader(ToHeader.NAME)).thenReturn(toHeader);
        when(toHeader.getAddress()).thenReturn(toAddress);
        when(toAddress.getURI()).thenReturn(toAddressURI);
        when(sipUtils.getCanonicalizedURI(toAddressURI)).thenReturn(toAddressURI);
        when(request.getRequestURI()).thenReturn(requestURI);
        when(sipUtils.getCanonicalizedURI(requestURI)).thenReturn(requestURI);
    }

    @Test
    public void testBadAddressUri() throws Exception {
        when(toAddressURI.isSipURI()).thenReturn(false);

        testException();
    }

    @Test
    public void testDifferentURIs() throws Exception {
        when(toAddressURI.isSipURI()).thenReturn(true);
        when(toAddressURI.getHost()).thenReturn("domain1.org");
        when(requestURI.getHost()).thenReturn("domain2.org");

        testException();
    }

    @Test
    public void testValidate() throws Exception {
        when(toAddressURI.isSipURI()).thenReturn(true);
        when(toAddressURI.getHost()).thenReturn("domain1.org");
        when(requestURI.getHost()).thenReturn("domain1.org");

        ToHeaderValidator validator = new ToHeaderValidator();
        validator.sipUtils = sipUtils;
        validator.validateRequest(requestEvent);

        assertTrue(true);
    }

    private void testException() throws Exception {
        ToHeaderValidator validator = new ToHeaderValidator();
        validator.sipUtils = sipUtils;

        try {
            validator.validateRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(ex.getErrorCode(), new Integer(Response.NOT_FOUND));
        }
    }
}