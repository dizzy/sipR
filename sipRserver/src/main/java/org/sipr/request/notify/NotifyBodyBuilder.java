package org.sipr.request.notify;

import javax.sip.message.Request;
import java.text.ParseException;

public interface NotifyBodyBuilder {

    void addMessageBody(Request notifyRequest) throws ParseException;

    String getEventType();

}
