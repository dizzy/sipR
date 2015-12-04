package org.sipr.request.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.request.notify.NotifyContentBuilder;

import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

    @Mock
    CSeqHeader cSeqHeader;

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
        when(headerFactory.createCSeqHeader(Long.valueOf(55), Request.NOTIFY)).thenReturn(cSeqHeader);
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(binding);

        wrapper.response = 202;
        handler.headerFactory = headerFactory;
        handler.messageFactory = messageFactory;
        handler.requestBuilder = subscriptionRequestBuilder;
        handler.subscriptionBindingsService = bindingsService;
        handler.handleRequest(requestEvent);
        verify(dialog).sendRequest(transaction);
    }

    @Test
    public void testHandleRequestException() throws Exception {
        when(subscriptionRequestBuilder.getSubscriptionRequest(requestEvent)).thenReturn(wrapper);
        when(messageFactory.createResponse(202, request)).thenThrow(new ParseException("", 1));

        wrapper.response = 202;
        handler.headerFactory = headerFactory;
        handler.messageFactory = messageFactory;
        handler.requestBuilder = subscriptionRequestBuilder;
        handler.subscriptionBindingsService = bindingsService;

        handler.handleRequest(requestEvent);
        verifyNoMoreInteractions(bindingsService);
    }

    @Test
    public void testUnknownEvent() throws Exception {
        SubscriptionRequest unknownWrapper = new SubscriptionRequest();
        unknownWrapper.eventHeader = unknownEventHeader;
        unknownWrapper.requestEvent = requestEvent;
        unknownWrapper.serverTransaction = serverTransaction;
        when(subscriptionRequestBuilder.getSubscriptionRequest(requestEvent)).thenReturn(unknownWrapper);
        when(messageFactory.createResponse(Response.NOT_IMPLEMENTED, request)).thenReturn(response);

        handler.requestBuilder = subscriptionRequestBuilder;
        handler.messageFactory = messageFactory;
        handler.handleRequest(requestEvent);

        verify(messageFactory).createResponse(501, request);
        verify(serverTransaction).sendResponse(response);
        verifyNoMoreInteractions(messageFactory, serverTransaction);

        when(messageFactory.createResponse(Response.NOT_IMPLEMENTED, request)).thenThrow(new ParseException("", 1));
        handler.handleRequest(requestEvent);
        verifyNoMoreInteractions(serverTransaction);
    }

    @Test
    public void testUnsubscribe() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(binding);
        when(binding.getCseq()).thenReturn(new Long(55));
        wrapper.isUnsubscribe = true;
        handler.updateSubscriptionStorage(wrapper);

        verify(binding).setCallId("call-id-dizzy");
        verify(binding).setExpires(5);
        verify(bindingsService).deleteSubscription(binding);
    }

    @Test
    public void testRefreshBinding() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(binding);
        when(binding.getCseq()).thenReturn(new Long(55));
        wrapper.isUnsubscribe = false;
        handler.updateSubscriptionStorage(wrapper);

        verify(binding).setCallId("call-id-dizzy");
        verify(binding).setExpires(5);
        verify(binding).setCseq(56);
        verify(bindingsService).saveSubscription(binding);
    }

    @Test
    public void testInitialSubscribe() throws Exception {
        when(bindingsService.findByContactAndType("sip:dizzy;transport=tcp", "as-feature-event")).thenReturn(null);
        when(bindingsService.createSubscription("dizzy", "sip:dizzy;transport=tcp", "call-id-dizzy", 1, 5, "as-feature-event")).thenReturn(newBinding);

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
        when(headerFactory.createCSeqHeader(Long.valueOf(55), Request.NOTIFY)).thenReturn(cSeqHeader);

        handler.headerFactory = headerFactory;
        wrapper.isUnsubscribe = finished;
        handler.addNotifyRequestHeaders(notifyRequest, wrapper, binding);
        verify(notifyRequest).setHeader(eventHeader);
    }
}
