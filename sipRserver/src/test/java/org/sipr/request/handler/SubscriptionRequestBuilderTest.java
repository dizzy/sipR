package org.sipr.request.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.utils.SipUtils;

import javax.sip.Dialog;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipProvider;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionRequestBuilderTest {

    @Mock
    SipUtils sipUtils;

    @Mock
    RequestEvent requestEvent;

    @Mock
    SipProvider sipProvider;

    @Mock
    Request request;

    @Mock
    EventHeader eventHeader;

    @Mock
    CSeqHeader cSeqHeader;

    @Mock
    CallIdHeader callIdHeader;

    @Mock
    ExpiresHeader expiresHeader;

    @Mock
    ExpiresHeader newExpiresHeader;

    @Mock
    ServerTransaction serverTransaction;

    @Mock
    Dialog dialog;

    @Before
    public void setup()throws Exception {
        when(requestEvent.getSource()).thenReturn(sipProvider);
        when(requestEvent.getRequest()).thenReturn(request);
        when(request.getHeader(EventHeader.NAME)).thenReturn(eventHeader);
        when(eventHeader.getEventType()).thenReturn("as-feature-event");

        when(cSeqHeader.getSeqNumber()).thenReturn(Long.valueOf(53));
        when(request.getHeader(CSeqHeader.NAME)).thenReturn(cSeqHeader);

        when(callIdHeader.getCallId()).thenReturn("call-id-dizzy");
        when(request.getHeader(CallIdHeader.NAME)).thenReturn(callIdHeader);

        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(expiresHeader);

        when(sipUtils.createExpiresHeader(22)).thenReturn(newExpiresHeader);
        when(newExpiresHeader.getExpires()).thenReturn(22);

        when(sipUtils.getFirstContactUri(request)).thenReturn("sip:dizzy;transport=tcp");
        when(sipUtils.extractAuthUser(request)).thenReturn("dizzy");
    }

    @Test
    public void testGetSubscriptionRequest() throws Exception {
        when(requestEvent.getDialog()).thenReturn(null);
        when(expiresHeader.getExpires()).thenReturn(5);

        SubscriptionRequest sRequest = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(53, sRequest.getCSeq());
        assertEquals("call-id-dizzy", sRequest.getCallId());
        assertEquals(Response.ACCEPTED, sRequest.getResponse());
        assertEquals(request, sRequest.getRequest());
        assertEquals(eventHeader, sRequest.getEventHeader());
        assertEquals(expiresHeader, sRequest.getExpiresHeader());
        assertEquals(sipProvider, sRequest.getSipProvider());
        assertEquals(5, sRequest.getExpires());
        assertEquals("as-feature-event", sRequest.getEventType());
        assertEquals("sip:dizzy;transport=tcp", sRequest.getContactUri());
        assertEquals("dizzy", sRequest.getUser());
    }

    @Test
    public void testResponses() throws Exception {
        when(requestEvent.getDialog()).thenReturn(null);
        SubscriptionRequest request = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(Response.ACCEPTED, request.getResponse());
        assertEquals(true, request.isInitialSubscribe());

        when(requestEvent.getDialog()).thenReturn(dialog);
        request = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(Response.OK, request.getResponse());
        assertEquals(false, request.isInitialSubscribe());
    }

    @Test
    public void testUnsubscribe() throws Exception {
        when(expiresHeader.getExpires()).thenReturn(0);
        SubscriptionRequest request = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(true, request.isUnsubscribe());

        when(expiresHeader.getExpires()).thenReturn(5);
        request = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(false, request.isUnsubscribe());
    }

    @Test
    public void testGetDialog() throws Exception {
        when(requestEvent.getDialog()).thenReturn(null);
        when(serverTransaction.getDialog()).thenReturn(dialog);
        when(requestEvent.getServerTransaction()).thenReturn(serverTransaction);

        SubscriptionRequest sRequest = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(dialog, sRequest.getDialog());
        assertEquals(serverTransaction, sRequest.getServerTransaction());
    }

    @Test
    public void testNullExpiresHeader() throws Exception {
        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(null);
        SubscriptionRequest sRequest = getBuilder().getSubscriptionRequest(requestEvent);
        assertEquals(newExpiresHeader, sRequest.getExpiresHeader());
    }

    private SubscriptionRequestBuilder getBuilder() {
        SubscriptionRequestBuilder builder = new SubscriptionRequestBuilder();
        builder.sipUtils = sipUtils;
        builder.serverExpire = 22;
        return builder;
    }
}