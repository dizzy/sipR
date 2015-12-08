package org.sipr.core.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.AuthDetails;
import org.sipr.core.service.AuthenticationService;
import org.sipr.utils.SipUtils;

import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SipDigestAuthenticationServiceTest {

    @Mock
    Request request;

    @Mock
    AuthorizationHeader authorizationHeader;

    @Mock
    URI uri;

    @Mock
    WWWAuthenticateHeader authHeader;

    @Mock
    HeaderFactory headerFactory;

    @Mock
    AuthenticationService authenticationService;

    @Mock
    SipUtils sipUtils;

    @Test
    public void testAuthenticate() throws Exception {
        when(request.getHeader(AuthorizationHeader.NAME)).thenReturn(authorizationHeader);
        when(request.getMethod()).thenReturn("INVITE");
        when(authorizationHeader.getUsername()).thenReturn("dizzy");
        when(authorizationHeader.getRealm()).thenReturn("sipr");
        when(authorizationHeader.getURI()).thenReturn(uri);
        when(authorizationHeader.getAlgorithm()).thenReturn("MD5");
        when(authorizationHeader.getNonce()).thenReturn("nonce");
        when(authorizationHeader.getCNonce()).thenReturn("cnonce");
        when(authorizationHeader.getResponse()).thenReturn("response");

        SipDigestAuthenticationService service = new SipDigestAuthenticationService();
        service.authenticationService = authenticationService;
        service.sipUtils = sipUtils;

        AuthDetails details = service.createAuthDetails(request);
        assertEquals("dizzy", details.getUsername());
        assertEquals("sipr", details.getRealm());
        assertEquals(uri.toString(), details.getUri());
        assertEquals("MD5", details.getAlgorithm());
        assertEquals("MD5", details.getAlgorithm());
        assertEquals("nonce", details.getNonce());
        assertEquals("cnonce", details.getCnonce());
        assertEquals("response", details.getResponse());

        when(sipUtils.extractAuthUser(request)).thenReturn("200");
        when(authenticationService.authenticate(details, "200")).thenReturn(false);
        assertFalse(service.authenticateRequest(request));
    }

    @Test
    public void testCreateAuthHeader() throws Exception {
        when(headerFactory.createWWWAuthenticateHeader("Digest")).thenReturn(authHeader);
        when(authenticationService.getRealm()).thenReturn("sipr");
        when(authenticationService.generateNonce()).thenReturn("nonce");
        when(authenticationService.getAlgorithm()).thenReturn("MD5");

        SipDigestAuthenticationService service = new SipDigestAuthenticationService();
        service.authenticationService = authenticationService;
        service.headerFactory = headerFactory;

        service.createAuthHeader();
        verify(authHeader).setParameter("realm", "sipr");
        verify(authHeader).setParameter("nonce", "nonce");
        verify(authHeader).setParameter("opaque", "");
        verify(authHeader).setParameter("stale", "FALSE");
        verify(authHeader).setParameter("algorithm", "MD5");
    }
}