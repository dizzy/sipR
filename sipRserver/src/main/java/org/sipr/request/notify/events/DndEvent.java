package org.sipr.request.notify.events;

import org.sipr.core.domain.UserPresence;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static java.lang.String.format;

@XmlRootElement(name = "SetDoNotDisturb", namespace = "http://www.ecma-international.org/standards/ecma-323/csta/ed3")
public class DndEvent {

    public static final String DND_EVENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>%s</device>\r\n" +
            "  <doNotDisturbOn>%s</doNotDisturbOn>\r\n" +
            "</DoNotDisturbEvent>";

    boolean dnd;

    @XmlElement(name = "doNotDisturbOn")
    public void setDnd(boolean dnd) {
        this.dnd = dnd;
    }

    public boolean isDnd() {
        return dnd;
    }

    public String buildEventResponse(UserPresence presence) {
        presence.setDnd(dnd);
        return getDndEvent(presence);
    }

    public static String getDndEvent(UserPresence presence) {
        return format(DND_EVENT, presence.getUsername(), presence.isDndEnabled());
    }
}
