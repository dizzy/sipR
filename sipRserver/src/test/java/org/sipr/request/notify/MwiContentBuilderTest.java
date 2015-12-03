package org.sipr.request.notify;

import gov.nist.javax.sip.message.ContentImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sip.RequestEvent;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExtensionHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MwiContentBuilderTest {

    @Mock
    ExtensionHeader waiting;

    @Mock
    ExtensionHeader account;

    @Mock
    ExtensionHeader message;

    @Mock
    HeaderFactory headerFactory;

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request subscribeRequest;

    @Mock
    Request notifyRequest;

    @Mock
    ContentTypeHeader contentTypeHeader;

    @Mock
    ContentImpl content;

    @Test
    public void testAddContent() throws Exception {
        // todo finalize when MailboxService implemented
        when(headerFactory.createHeader("Messages-Waiting", "yes")).thenReturn(waiting);
        when(headerFactory.createHeader("Message-Account", "dizzy")).thenReturn(account);
        when(headerFactory.createHeader("Voice-Message", "8/11 (0/0)")).thenReturn(message);
        when(requestEvent.getRequest()).thenReturn(subscribeRequest);
        when(headerFactory.createContentTypeHeader("application", "simple-message-summary")).thenReturn(contentTypeHeader);
    }

    @Test
    public void testGetEventType() throws Exception {
        MwiContentBuilder mwi = new MwiContentBuilder();
        assertEquals("message-summary", mwi.getEventType());
    }
}