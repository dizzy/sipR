package org.sipr.core.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.User;
import org.sipr.core.service.UserService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DigestAuthenticationServiceTest {

    @Mock
    UserService userService;

    @Mock
    User user;

    DigestAuthenticationService service;

    @Before
    public void setUp() throws Exception {
        DigestAuthenticationService digestService = new DigestAuthenticationService();

        when(user.getSipPassword()).thenReturn("e2e047fd0adc3246d8998c8f3f021328");
        when(userService.getUser("200")).thenReturn(user);

        digestService.userService = userService;
        service = spy(digestService);
        doReturn(1L).when(service).currentTimeInMillis();
        doReturn(1L).when(service).getPad();
    }

    @Test
    public void testGenerateNonce() throws Exception {
        assertEquals("6512bd43d9caa6e02c990b0a82652dca", service.generateNonce());
    }

    @Test
    public void testAuthenticate() throws Exception {
        assertFalse(service.authenticate(null, null));

        DigestAuthDetails details = new DigestAuthDetails("200", "sipr", "200@sipr.org", "MD5",
                "6512bd43d9caa6e02c990b0a82652dca", "6512bd43d9caa6e02c990b0a82652dca",
                "342019fc8b0793047f350ad70c408cf0", "REGISTER");
        assertFalse(service.authenticate(details, "201"));
        assertTrue(service.authenticate(details, "200"));
    }
}