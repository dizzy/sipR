package org.sipr.request.notify.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.UserPresence;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.mockito.Mockito.*;
import static org.custommonkey.xmlunit.XMLAssert.*;

@RunWith(MockitoJUnitRunner.class)
public class ForwardEventTest {

    @Mock
    UserPresence userPresence;

    @Test
    public void testBuildForwardImmediate() throws Exception {
        when(userPresence.getUsername()).thenReturn("dizzy");
        when(userPresence.isFwdImmediate()).thenReturn(true);
        when(userPresence.getFwdImmediateNumber()).thenReturn("111");

        ForwardEvent event = new ForwardEvent();
        event.setForwardingType(UserPresence.FORWARD_IMMEDIATE);
        event.setForwardStatus(true);
        event.setForwardTo("555");
        assertEquals("555", event.getForwardTo());
        assertEquals(UserPresence.FORWARD_IMMEDIATE, event.getForwardingType());
        assertTrue(event.getForwardStatus());
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardImmediate</forwardingType>\n" +
                "  <forwardStatus>true</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>0</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdImmediateNumber("555");

        event.setForwardStatus(false);
        assertEquals("", event.getForwardTo());
        when(userPresence.isFwdImmediate()).thenReturn(false);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardImmediate</forwardingType>\n" +
                "  <forwardStatus>false</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>0</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdImmediateNumber("");
    }

    @Test
    public void testBuildForwardOnBusy() throws Exception {
        when(userPresence.getUsername()).thenReturn("dizzy");
        when(userPresence.isFwdBusy()).thenReturn(true);
        when(userPresence.getFwdBusyNumber()).thenReturn("111");

        ForwardEvent event = new ForwardEvent();
        event.setForwardingType(UserPresence.FORWARD_BUSY);
        event.setForwardStatus(true);
        event.setForwardTo("555");
        assertEquals("555", event.getForwardTo());
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardBusy</forwardingType>\n" +
                "  <forwardStatus>true</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>0</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdBusyNumber("555");

        event.setForwardStatus(false);
        assertEquals("", event.getForwardTo());
        when(userPresence.isFwdBusy()).thenReturn(false);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardBusy</forwardingType>\n" +
                "  <forwardStatus>false</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>0</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdBusyNumber("");
    }

    @Test
    public void testBuildForwardNoAnswer() throws Exception {
        when(userPresence.getUsername()).thenReturn("dizzy");
        when(userPresence.isFwdNoAnswer()).thenReturn(true);
        when(userPresence.getFwdNoAnswerNumber()).thenReturn("111");
        when(userPresence.getFwdNoAnswerRingCount()).thenReturn(9);

        ForwardEvent event = new ForwardEvent();
        event.setForwardingType(UserPresence.FORWARD_NO_ANSWER);
        event.setForwardStatus(true);
        event.setForwardTo("555");
        event.setRingCount(15);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardNoAns</forwardingType>\n" +
                "  <forwardStatus>true</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>9</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdNoAnswerNumber("555");
        verify(userPresence).setFwdNoAnswerRingCount(15);

        event.setForwardStatus(false);
        assertEquals("", event.getForwardTo());
        when(userPresence.isFwdNoAnswer()).thenReturn(false);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <forwardingType>forwardNoAns</forwardingType>\n" +
                "  <forwardStatus>false</forwardStatus>\n" +
                "  <forwardTo>111</forwardTo>\n" +
                "  <ringCount>9</ringCount>\n" +
                "</ForwardingEvent>", event.buildEventResponse(userPresence));
        verify(userPresence).setFwdNoAnswerNumber("");
    }

    @Test
    public void testAnswerWrongFwdType() {
        ForwardEvent event = new ForwardEvent();
        assertNull(event.buildEventResponse(userPresence));
    }
}