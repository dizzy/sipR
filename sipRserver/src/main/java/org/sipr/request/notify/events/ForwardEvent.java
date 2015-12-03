package org.sipr.request.notify.events;

import org.sipr.core.domain.UserPresence;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static java.lang.String.format;
import static org.sipr.core.domain.UserPresence.*;

@XmlRootElement(name = "SetForwarding", namespace = "http://www.ecma-international.org/standards/ecma-323/csta/ed3")
public class ForwardEvent {

    public static final String FORWARD_EVENT =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
                    "<ForwardingEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
                    "  <device>%s</device>\r\n" +
                    "  <forwardingType>%s</forwardingType>\r\n" +
                    "  <forwardStatus>%s</forwardStatus>\r\n" +
                    "  <forwardTo>%s</forwardTo>\r\n" +
                    "  <ringCount>%s</ringCount>\r\n" +
                    "</ForwardingEvent>";

    String forwardingType = "";
    boolean forwardStatus;
    String forwardTo;
    int ringCount;

    public String getForwardTo() {
        if (forwardStatus) {
            return forwardTo;
        }
        return "";
    }

    @XmlElement(name = "forwardDN")
    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public String getForwardingType() {
        return forwardingType;
    }

    @XmlElement
    public void setForwardingType(String forwardingType) {
        this.forwardingType = forwardingType;
    }

    public boolean getForwardStatus() {
        return forwardStatus;
    }

    @XmlElement(name = "activateForward")
    public void setForwardStatus(boolean forwardStatus) {
        this.forwardStatus = forwardStatus;
    }

    public int getRingCount() {
        return ringCount;
    }

    @XmlElement
    public void setRingCount(int ringCount) {
        this.ringCount = ringCount;
    }

    public String buildEventResponse(UserPresence presence) {
        switch (forwardingType) {
            case FORWARD_IMMEDIATE :
                presence.setFwdImmediateNumber(getForwardTo());
                return getForwardImmediateEvent(presence);
            case FORWARD_BUSY :
                presence.setFwdBusyNumber(getForwardTo());
                return getForwardOnBusyEvent(presence);
            case FORWARD_NO_ANSWER :
                presence.setFwdNoAnswerNumber(getForwardTo());
                presence.setFwdNoAnswerRingCount(getRingCount());
                return getForwardNoAnswerEvent(presence);
            default : return null;
        }
    }

    public static String getForwardImmediateEvent(UserPresence presence) {
        return format(FORWARD_EVENT, presence.getUsername(),
                FORWARD_IMMEDIATE, presence.isFwdImmediate(), presence.getFwdImmediateNumber(), 0);
    }

    public static String getForwardOnBusyEvent(UserPresence presence) {
        return format(FORWARD_EVENT, presence.getUsername(),
                FORWARD_BUSY, presence.isFwdBusy(), presence.getFwdBusyNumber(), 0);
    }

    public static String getForwardNoAnswerEvent(UserPresence presence) {
        return format(FORWARD_EVENT, presence.getUsername(),
                FORWARD_NO_ANSWER, presence.isFwdNoAnswer(), presence.getFwdNoAnswerNumber(), presence.getFwdNoAnswerRingCount());
    }

}
