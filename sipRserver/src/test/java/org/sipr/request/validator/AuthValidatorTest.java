package org.sipr.request.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.service.SipAuthenticationService;
import org.sipr.core.sip.request.processor.RequestException;

import javax.sip.RequestEvent;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthValidatorTest {

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    SipAuthenticationService sipAuthenticationService;

    @Mock
    WWWAuthenticateHeader authenticateHeader;

    @Mock
    AuthorizationHeader authorizationHeader;

    @Before
    public void setUp() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
    }

    @Test
    public void testNoAuthHeader() throws Exception {
        when(request.getHeader(AuthorizationHeader.NAME)).thenReturn(null);
        when(sipAuthenticationService.createAuthHeader()).thenReturn(authenticateHeader);
        AuthValidator authValidator = new AuthValidator();
        authValidator.sipAuthenticationService = sipAuthenticationService;

        try {
            authValidator.validateRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertTrue(ex.getHeaders().contains(authenticateHeader));
            assertEquals(ex.getErrorCode(), new Integer(Response.UNAUTHORIZED));
        }
    }

    @Test
    public void testInvalidCredentials() throws Exception {
        when(request.getHeader(AuthorizationHeader.NAME)).thenReturn(authorizationHeader);
        when(sipAuthenticationService.authenticateRequest(request)).thenReturn(false);
        AuthValidator authValidator = new AuthValidator();
        authValidator.sipAuthenticationService = sipAuthenticationService;

        try {
            authValidator.validateRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(ex.getErrorCode(), new Integer(Response.NOT_FOUND));
        }
    }

    @Test
    public void testValidate() throws Exception {
        when(request.getHeader(AuthorizationHeader.NAME)).thenReturn(authorizationHeader);
        when(sipAuthenticationService.authenticateRequest(request)).thenReturn(true);
        AuthValidator authValidator = new AuthValidator();
        authValidator.sipAuthenticationService = sipAuthenticationService;
        authValidator.validateRequest(requestEvent);
        assertTrue(true);
    }
}