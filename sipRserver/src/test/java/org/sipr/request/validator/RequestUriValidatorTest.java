package org.sipr.request.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.service.SipDomainService;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.utils.SipUtils;

import javax.sip.RequestEvent;
import javax.sip.address.SipURI;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestUriValidatorTest {

    @Mock
    SipDomainService sipDomainService;

    @Mock
    SipUtils sipUtils;

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    SipURI uri;

    @Before
    public void setUp() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
        when(sipDomainService.isValidDomain("sipr.org")).thenReturn(true);
        when(sipDomainService.isValidDomain("baddomain.org")).thenReturn(false);
    }

    @Test
    public void testBadUri() throws Exception {
        when(sipUtils.getCanonicalizedURI(request.getRequestURI())).thenReturn(null);

        testException();

        when(uri.isSipURI()).thenReturn(false);
        when(sipUtils.getCanonicalizedURI(request.getRequestURI())).thenReturn(uri);

        testException();
    }

    @Test
    public void testInvalidDomain() throws Exception {
        when(uri.isSipURI()).thenReturn(true);
        when(uri.getHost()).thenReturn("baddomain.org");
        when(sipUtils.getCanonicalizedURI(request.getRequestURI())).thenReturn(uri);

       testException();
    }

    private void testException() {
        RequestUriValidator validator = new RequestUriValidator();
        validator.sipDomainService = sipDomainService;
        validator.sipUtils = sipUtils;

        try {
            validator.validateRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(ex.getErrorCode(), new Integer(Response.BAD_REQUEST));
        }
    }

    @Test
    public void testValidate() throws Exception {
        when(uri.isSipURI()).thenReturn(true);
        when(uri.getHost()).thenReturn("sipr.org");
        when(sipUtils.getCanonicalizedURI(request.getRequestURI())).thenReturn(uri);
        RequestUriValidator validator = new RequestUriValidator();
        validator.sipDomainService = sipDomainService;
        validator.sipUtils = sipUtils;
        validator.validateRequest(requestEvent);
        assertTrue(true);
    }
}