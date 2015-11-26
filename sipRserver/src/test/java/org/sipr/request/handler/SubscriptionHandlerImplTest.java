package org.sipr.request.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.request.notify.NotifyContentBuilder;

import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionHandlerImplTest {

    @Mock
    SubscriptionRequestBuilderImpl requestBuilder;

    @Mock
    NotifyContentBuilder contentBuilder;

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    EventHeader eventHeader;

    @Mock
    EventHeader unknownEventHeader;

    @Mock
    ExpiresHeader expiresHeader;

    @Mock
    SubscriptionBindingsService bindingsService;

    @Mock
    SubscriptionBinding binding;

    @Mock
    SubscriptionBinding newBinding;

    @Mock
    MessageFactory messageFactory;

    @Mock
    Response response;

    @Mock
    ToHeader toHeader;

    @Mock
    HeaderFactory headerFactory;

    @Mock
    SubscriptionStateHeader active;

    @Mock
    SubscriptionStateHeader terminated;

    @Mock
    Request notifyRequest;

    @Mock
    SubscriptionRequestBuilder subscriptionRequestBuilder;

    @Mock
    ServerTransaction serverTransaction;

    @Mock
    Dialog dialog;

    @Mock
    SipProvider sipProvider;

    @Mock
    ClientTransaction transaction;

    SubscriptionRequest wrapper = new SubscriptionRequest();

    List<NotifyContentBuilder> notifyBuilders = new ArrayList<>();

    SubscriptionHandlerImpl handler = new SubscriptionHandlerImpl();

    @Before
    public void init() throws Exception {
        when(contentBuilder.getEventType()).thenReturn("as-feature-event");
        when(requestEvent.getRequest()).thenReturn(request);

        when(request.getHeader(EventHeader.NAME)).thenReturn(eventHeader);
        when(eventHeader.getEventType()).thenReturn("as-feature-event");

        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(expiresHeader);
        when(expiresHeader.getExpires()).thenReturn(5);

        when(serverTransaction.getDialog()).thenReturn(dialog);
        when(dialog.createRequest(Request.NOTIFY)).thenReturn(notifyRequest);

        when(sipProvider.getNewClientTransaction(notifyRequest)).thenReturn(transaction);

        wrapper.eventHeader = eventHeader;
        wrapper.expiresHeader = expiresHeader;
        wrapper.contactUri = "sip:dizzy;transport=tcp";
        wrapper.requestEvent = requestEvent;
        wrapper.callId = "call-id-dizzy";
        wrapper.authUser = "dizzy";
        wrapper.expiresHeader = expiresHeader;
        wrapper.cSeq = 55;
        wrapper.serverTransaction = serverTransaction;
        wrapper.sipProvider = sipProvider;

        notifyBuilders.add(contentBuilder);
        handler.notifyBuilders = notifyBuilders;
        handler.subscriptionBindingsService = bindingsService;
        handler.init();
    }

    @Test
    public void testHandleRequest() throws Exception {
        when(subscriptionRequestBuilder.getSubscriptionRequest(requestEvent)).thenReturn(wrapper);
        when(messageFactory.createResponse(202, request)).thenReturn(response);
        when(response.getHeader(ToHeader.NAME)).thenReturn(toHeader);
        when(headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE)).thenReturn(active);
        when(headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.TERMINATED)).thenReturn(terminated);

        wrapper.response = 202;
        handler.headerFactory = headerFactory;
        handler.messageFactory = messageFactory;
        handler.requestBuilder = subscriptionRequestBuilder;
        handler.handleRequest(requestEvent);
        verify(dialog).sendRequest(transaction);

        when(sipProvider.getNewClientTransaction(notifyRequest)).thenThrow(new TransactionUnavailableException());
        try {
            handler.handleRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(Integer.valueOf(Response.SERVER_INTERNAL_ERROR), ex.getErrorCode());
        }

    }

    @Test
    public void testUnknownEvent() {
        SubscriptionRequest wrapper = new SubscriptionRequest();
        wrapper.eventHeader = unknownEventHeader;

        try {
            handler.getNotifier(wrapper);
            fail();
        } catch (RequestException ex) {
            assertEquals(Integer.valueOf(Response.NOT_IMPLEMENTED), ex.getErrorCode());
        }
    }

    @Test
    public void testGetNotifier() throws Exception {
        SubscriptionRequest wrapper = new SubscriptionRequest();
        wrapper.eventHeader = eventHeader;
        assertEquals(contentBuilder, handler.getNotifier(wrapper));
    }

    @Test
    public void testUnsubscribe() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(binding);
        wrapper.isUnsubscribe = true;
        handler.updateSubscriptionStorage(wrapper);

        verify(binding).setCallId("call-id-dizzy");
        verify(binding).setExpires(5);
        verify(binding).setCseq(55);
        verify(bindingsService).deleteSubscription(binding);
    }

    @Test
    public void testRefreshBinding() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(binding);
        wrapper.isUnsubscribe = false;
        handler.updateSubscriptionStorage(wrapper);

        verify(binding).setCallId("call-id-dizzy");
        verify(binding).setExpires(5);
        verify(binding).setCseq(55);
        verify(bindingsService).saveSubscription(binding);
    }

    @Test
    public void testInitialSubscribe() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(null);
        when(bindingsService.createSubscription("dizzy", "sip:dizzy;transport=tcp", "call-id-dizzy", 55, 5, "as-feature-event")).thenReturn(newBinding);

        wrapper.isUnsubscribe = false;
        handler.updateSubscriptionStorage(wrapper);
        verify(bindingsService).saveSubscription(newBinding);

    }

    @Test
    public void testCreateSubscriptionResponse() throws Exception {
        when(messageFactory.createResponse(202, request)).thenReturn(response);
        when(response.getHeader(ToHeader.NAME)).thenReturn(toHeader);
        wrapper.response = 202;
        handler.messageFactory = messageFactory;
        handler.createSubscriptionResponse(wrapper);
        verify(response).addHeader(expiresHeader);
    }

    @Test
    public void testAddTerminatedNotifyRequestHeaders() throws Exception {
        testAddHeaders(true);
        verify(notifyRequest).addHeader(terminated);
    }

    @Test
    public void testAddActiveNotifyRequestHeaders() throws Exception {
        testAddHeaders(false);
        verify(notifyRequest).addHeader(active);
    }

    void testAddHeaders(boolean finished) throws Exception {
        when(headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE)).thenReturn(active);
        when(headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.TERMINATED)).thenReturn(terminated);

        handler.headerFactory = headerFactory;
        wrapper.isUnsubscribe = finished;
        handler.addNotifyRequestHeaders(notifyRequest, wrapper);
        verify(notifyRequest).setHeader(eventHeader);
    }
}