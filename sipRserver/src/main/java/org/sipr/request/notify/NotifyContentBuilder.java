package org.sipr.request.notify;

import javax.sip.message.Request;
import java.text.ParseException;

public interface NotifyContentBuilder {

    void addContent(Request notifyRequest, String user, byte[] rawContent) throws ParseException;

    String getEventType();

}
