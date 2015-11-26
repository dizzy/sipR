package org.sipr.request.notify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.sipr.request.handler.SubscriptionRequest;

import javax.sip.RequestEvent;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DndContentBuilderTest {

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request subscribeRequest;

    @Mock
    Request notifyRequest;

    @Mock
    NotifySender notifySender;

    @Mock
    UserPresence userPresence;

    @Mock
    HeaderFactory headerFactory;

    @Mock
    ContentTypeHeader contentTypeHeader;

    @Mock
    UserPresenceService presenceService;

    DndContentBuilder dnd = new DndContentBuilder();

    private static final String SET_DND_FALSE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<SetDoNotDisturb xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>5559430902</device>\n\r\n" +
            "  <doNotDisturbOn>false</doNotDisturbOn>\r\n" +
            "</SetDoNotDisturb>\r\n";

    private static final String SET_DND_TRUE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<SetDoNotDisturb xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>5559430902</device>\n\r\n" +
            "  <doNotDisturbOn>true</doNotDisturbOn>\r\n" +
            "</SetDoNotDisturb>\r\n";

    @Before
    public void init() throws Exception {
        when(requestEvent.getRequest()).thenReturn(subscribeRequest);
        when(presenceService.getPresence("dizzy")).thenReturn(userPresence);
        when(headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml")).thenReturn(contentTypeHeader);

        dnd.presenceService = presenceService;
        dnd.headerFactory = headerFactory;
        dnd.sender = notifySender;
    }

    @Test
    public void testSetAvailable() throws Exception {
        when(subscribeRequest.getRawContent()).thenReturn(SET_DND_FALSE.getBytes(StandardCharsets.UTF_8));
        testAction(false);
    }

    @Test
    public void testSetDnd() throws Exception {
        when(subscribeRequest.getRawContent()).thenReturn(SET_DND_TRUE.getBytes(StandardCharsets.UTF_8));
        testAction(true);
    }

    void testAction(boolean dndFlag) throws Exception {
        SubscriptionRequest subscriptionWrapper = new SubscriptionRequest(requestEvent, "dizzy");
        dnd.addContent(notifyRequest, subscriptionWrapper);
        String content = String.format(DndContentBuilder.DND_EVENT, "dizzy", dndFlag);
        verify(notifyRequest).setContent(content, contentTypeHeader);
        verify(notifySender).sendNotifyToAllSubscribers(subscriptionWrapper, contentTypeHeader, content);
    }

    @Test
    public void testInitialSubscribe() throws Exception {
        when(subscribeRequest.getRawContent()).thenReturn("".getBytes(StandardCharsets.UTF_8));
        SubscriptionRequest subscriptionWrapper = new SubscriptionRequest(requestEvent, "dizzy");

        when(userPresence.isDndEnabled()).thenReturn(false);
        dnd.addContent(notifyRequest, subscriptionWrapper);
        verify(notifyRequest).setContent(String.format(DndContentBuilder.DND_EVENT, "dizzy", false), contentTypeHeader);

        when(userPresence.isDndEnabled()).thenReturn(true);
        dnd.addContent(notifyRequest, subscriptionWrapper);
        verify(notifyRequest).setContent(String.format(DndContentBuilder.DND_EVENT, "dizzy", true), contentTypeHeader);
    }

    @Test
    public void testGetEventType() throws Exception {
        DndContentBuilder dnd = new DndContentBuilder();
        assertEquals("as-feature-event", dnd.getEventType());
    }
}