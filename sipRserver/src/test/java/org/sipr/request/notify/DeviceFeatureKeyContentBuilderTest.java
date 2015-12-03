package org.sipr.request.notify;

import gov.nist.javax.sip.message.ContentImpl;
import gov.nist.javax.sip.message.MultipartMimeContentImpl;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.sipr.request.handler.SubscriptionRequest;
import org.sipr.request.notify.events.DndEvent;
import org.sipr.request.notify.events.ForwardEvent;
import org.sipr.request.notify.events.NotifyEventFactory;

import javax.sip.RequestEvent;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import javax.xml.transform.Source;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeviceFeatureKeyContentBuilderTest {

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
    ContentTypeHeader mixedContentTypeHeader;

    @Mock
    UserPresenceService presenceService;

    @Mock
    DndEvent dndEvent;

    @Mock
    ForwardEvent fwdEvent;

    @Mock
    NotifyEventFactory eventFactory;

    @Mock
    Source dndSource;

    @Mock
    ContentLengthHeader contentLengthHeader;

    @Mock
    SubscriptionRequest subscriptionRequest;

    private static final String SET_DND_FALSE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<SetDoNotDisturb xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>5559430902</device>\n\r\n" +
            "  <doNotDisturbOn>false</doNotDisturbOn>\r\n" +
            "</SetDoNotDisturb>\r\n";

    @Test
    public void testAddContent() throws Exception {
        when(subscribeRequest.getRawContent()).thenReturn(SET_DND_FALSE.getBytes(StandardCharsets.UTF_8));
        when(subscriptionRequest.getRequest()).thenReturn(subscribeRequest);
        when(subscriptionRequest.getUser()).thenReturn("dizzy");
        when(presenceService.getPresence("dizzy")).thenReturn(userPresence);
        when(eventFactory.createEvent(any())).thenReturn(dndEvent);
        when(headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml")).thenReturn(contentTypeHeader);
        when(dndEvent.buildEventResponse(userPresence)).thenReturn("DNDEventResponse");

        DeviceFeatureKeyContentBuilder fkBuilder = new DeviceFeatureKeyContentBuilder();
        fkBuilder.presenceService = presenceService;
        fkBuilder.headerFactory = headerFactory;
        fkBuilder.eventFactory = eventFactory;
        fkBuilder.sender = notifySender;

        fkBuilder.addContent(notifyRequest, subscriptionRequest);

        verify(presenceService).getPresence("dizzy");
        verify(dndEvent).buildEventResponse(userPresence);
        verify(presenceService).savePresence(userPresence);
        verify(notifyRequest).setContent("DNDEventResponse", contentTypeHeader);
        verify(notifySender).sendNotifyToAllSubscribers(subscriptionRequest, contentTypeHeader, "DNDEventResponse");

        when(subscribeRequest.getRawContent()).thenReturn("".getBytes(StandardCharsets.UTF_8));
        when(headerFactory.createContentTypeHeader("multipart", "mixed")).thenReturn(mixedContentTypeHeader);

        fkBuilder.addContent(notifyRequest, subscriptionRequest);

        verify(headerFactory).createContentTypeHeader("multipart", "mixed");
        verify(mixedContentTypeHeader).setParameter("boundary", "SipRBoundary");
    }

    @Test
    public void testGetEventResponse() throws Exception {
        DeviceFeatureKeyContentBuilder fkBuilder = new DeviceFeatureKeyContentBuilder();
        when(eventFactory.createEvent("DNDeventContent")).thenReturn(dndEvent);
        when(eventFactory.createEvent("FWDeventContent")).thenReturn(fwdEvent);
        when(eventFactory.createEvent("NOeventContent")).thenReturn(null);

        fkBuilder.eventFactory = eventFactory;
        when(dndEvent.buildEventResponse(userPresence)).thenReturn("DNDeventResponse");
        when(fwdEvent.buildEventResponse(userPresence)).thenReturn("FWDeventResponse");

        assertEquals("DNDeventResponse", fkBuilder.getEventResponse(userPresence, "DNDeventContent"));
        verify(dndEvent).buildEventResponse(userPresence);

        assertEquals("FWDeventResponse", fkBuilder.getEventResponse(userPresence, "FWDeventContent"));
        verify(fwdEvent).buildEventResponse(userPresence);

        assertNull(fkBuilder.getEventResponse(userPresence, "NOeventContent"));
    }

    @Test
    public void testCreateContent() throws Exception {
        DeviceFeatureKeyContentBuilder fkBuilder = new DeviceFeatureKeyContentBuilder();
        when(headerFactory.createContentLengthHeader("ResponseContent".length())).thenReturn(contentLengthHeader);
        when(headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml")).thenReturn(contentTypeHeader);
        fkBuilder.headerFactory = headerFactory;

        ContentImpl content = (ContentImpl) fkBuilder.createContent("ResponseContent");
        assertEquals("ResponseContent", (String) content.getContent());

        List<Header> extensionHeaders = IteratorUtils.toList(content.getExtensionHeaders());
        assertEquals(2, extensionHeaders.size());
        assertTrue(extensionHeaders.contains(contentTypeHeader));
        assertTrue(extensionHeaders.contains(contentLengthHeader));
    }

    @Test
    public void testGetInitialMultipartContent() throws Exception {
        when(contentTypeHeader.getParameter("boundary")).thenReturn("SipRBoundary");
        when(contentTypeHeader.toString()).thenReturn("Content-Type:application/x-as-feature-event+xml");
        when(contentLengthHeader.toString()).thenReturn("Content-Length:142");
        when(headerFactory.createContentTypeHeader("multipart", "mixed")).thenReturn(contentTypeHeader);
        when(headerFactory.createContentLengthHeader(anyInt())).thenReturn(contentLengthHeader);
        when(headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml")).thenReturn(contentTypeHeader);

        when(userPresence.getUsername()).thenReturn("dizzy");
        when(userPresence.isDndEnabled()).thenReturn(true);
        when(userPresence.isFwdImmediate()).thenReturn(true);
        when(userPresence.getFwdImmediateNumber()).thenReturn("700");
        when(userPresence.isFwdBusy()).thenReturn(true);
        when(userPresence.getFwdBusyNumber()).thenReturn("800");
        when(userPresence.isFwdNoAnswer()).thenReturn(true);
        when(userPresence.getFwdNoAnswerNumber()).thenReturn("900");
        when(userPresence.getFwdNoAnswerRingCount()).thenReturn(11);

        DeviceFeatureKeyContentBuilder fkBuilder = new DeviceFeatureKeyContentBuilder();
        fkBuilder.headerFactory = headerFactory;

        MultipartMimeContentImpl content = fkBuilder.getInitialMultipartContent(contentTypeHeader, userPresence);
        assertEquals("--SipRBoundary" +
                "Content-Type:application/x-as-feature-event+xml" +
                "Content-Length:142" +
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">" +
                "  <device>dizzy</device>" +
                "  <doNotDisturbOn>true</doNotDisturbOn>" +
                "</DoNotDisturbEvent>" +
                "--SipRBoundary" +
                "Content-Type:application/x-as-feature-event+xml" +
                "Content-Length:142" +
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">" +
                "  <device>dizzy</device>" +
                "  <forwardingType>forwardImmediate</forwardingType>" +
                "  <forwardStatus>true</forwardStatus>" +
                "  <forwardTo>700</forwardTo>" +
                "  <ringCount>0</ringCount>" +
                "</ForwardingEvent>" +
                "--SipRBoundary" +
                "Content-Type:application/x-as-feature-event+xml" +
                "Content-Length:142" +
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">" +
                "  <device>dizzy</device>" +
                "  <forwardingType>forwardBusy</forwardingType>" +
                "  <forwardStatus>true</forwardStatus>" +
                "  <forwardTo>800</forwardTo>" +
                "  <ringCount>0</ringCount>" +
                "</ForwardingEvent>" +
                "--SipRBoundary" +
                "Content-Type:application/x-as-feature-event+xml" +
                "Content-Length:142" +
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">" +
                "  <device>dizzy</device>" +
                "  <forwardingType>forwardNoAns</forwardingType>" +
                "  <forwardStatus>true</forwardStatus>" +
                "  <forwardTo>900</forwardTo>" +
                "  <ringCount>11</ringCount>" +
                "</ForwardingEvent>" +
                "--SipRBoundary--", content.toString().replaceAll("\\r\\n|\\r|\\n", ""));
    }

    @Test
    public void testGetEventType() throws Exception {
        DeviceFeatureKeyContentBuilder dnd = new DeviceFeatureKeyContentBuilder();
        assertEquals("as-feature-event", dnd.getEventType());
    }
}
