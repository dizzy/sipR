package org.sipr.request.processor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.sip.request.handler.SubscriptionHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.request.validator.AuthValidator;
import org.sipr.utils.SipMessageSender;

import javax.sip.RequestEvent;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscribeRequestProcessorTest {

    @Mock
    AuthValidator authValidator;

    @Mock
    RequestEvent requestEvent;

    @Mock
    SipMessageSender sipMessageSender;

    @Mock
    SubscriptionHandler subscriptionHandler;

    RequestException ex;

    List headers;

    @Mock
    WWWAuthenticateHeader authenticateHeader;

    @Mock
    Exception pex;

    @Before
    public void init() throws Exception {
        headers = Collections.singletonList(authenticateHeader);
        ex = new RequestException(Response.BAD_EXTENSION, headers);
    }

    @Test
    public void testValidatorRequestException() throws Exception {
        doThrow(ex).when(authValidator).validateRequest(requestEvent);
        SubscribeRequestProcessor processor = new SubscribeRequestProcessor();
        processor.authValidator = authValidator;
        processor.sipMessageSender = sipMessageSender;
        processor.processEvent(requestEvent);
        verify(sipMessageSender).sendResponse(requestEvent, ex.getErrorCode(), headers);
    }

    @Test
    public void testHandlerException() throws Exception {
        doNothing().when(authValidator).validateRequest(requestEvent);
        doThrow(ex).when(subscriptionHandler).handleRequest(requestEvent);
        SubscribeRequestProcessor processor = new SubscribeRequestProcessor();
        processor.authValidator = authValidator;
        processor.sipMessageSender = sipMessageSender;
        processor.subscriptionHandler = subscriptionHandler;
        processor.processEvent(requestEvent);
        verify(sipMessageSender).sendResponse(requestEvent, ex.getErrorCode(), headers);
    }

    @Test
    public void testType() throws Exception {
        SubscribeRequestProcessor processor = new SubscribeRequestProcessor();
        assertEquals(Request.SUBSCRIBE, processor.getRequestType());
    }
}