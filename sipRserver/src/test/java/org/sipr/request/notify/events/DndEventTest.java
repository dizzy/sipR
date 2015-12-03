package org.sipr.request.notify.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.UserPresence;

import static org.mockito.Mockito.*;
import static org.custommonkey.xmlunit.XMLAssert.*;

@RunWith(MockitoJUnitRunner.class)
public class DndEventTest {

    @Mock
    UserPresence dndUserPresence;

    @Test
    public void testBuildEventResponse() throws Exception {
        when(dndUserPresence.getUsername()).thenReturn("dizzy");

        when(dndUserPresence.isDndEnabled()).thenReturn(true);
        DndEvent event = new DndEvent();
        event.setDnd(true);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <doNotDisturbOn>true</doNotDisturbOn>\n" +
                "</DoNotDisturbEvent>", event.buildEventResponse(dndUserPresence));
        verify(dndUserPresence).setDnd(true);
        assertEquals(true, event.isDnd());

        when(dndUserPresence.isDndEnabled()).thenReturn(false);
        event = new DndEvent();
        event.setDnd(false);
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\n" +
                "  <device>dizzy</device>\n" +
                "  <doNotDisturbOn>false</doNotDisturbOn>\n" +
                "</DoNotDisturbEvent>", event.buildEventResponse(dndUserPresence));
        verify(dndUserPresence).setDnd(false);
        assertEquals(false, event.isDnd());
    }
}