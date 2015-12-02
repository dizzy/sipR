package org.sipr.request.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.sip.request.processor.RequestException;

import javax.sip.RequestEvent;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ProxyRequireHeader;
import javax.sip.header.UnsupportedHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequireHeaderValidatorTest {

    @Mock
    HeaderFactory headerFactory;

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    ProxyRequireHeader proxyRequireHeader;

    @Mock
    UnsupportedHeader unsupportedHeader;

    @Test
    public void testValidate() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
        when(request.getHeader(ProxyRequireHeader.NAME)).thenReturn(null);
        RequireHeaderValidator validator = new RequireHeaderValidator();
        validator.validateRequest(requestEvent);
        assertTrue(true);
    }

    @Test
    public void testRequireHeader() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
        when(request.getHeader(ProxyRequireHeader.NAME)).thenReturn(proxyRequireHeader);
        when(proxyRequireHeader.getOptionTag()).thenReturn("option");
        when(headerFactory.createUnsupportedHeader("option")).thenReturn(unsupportedHeader);
        RequireHeaderValidator validator = new RequireHeaderValidator();
        validator.headerFactory = headerFactory;

        try {
            validator.validateRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertTrue(ex.getHeaders().contains(unsupportedHeader));
            assertEquals(ex.getErrorCode(), new Integer(Response.BAD_EXTENSION));
        }
    }
}