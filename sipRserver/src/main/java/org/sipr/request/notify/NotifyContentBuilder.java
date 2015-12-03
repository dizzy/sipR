package org.sipr.request.notify;

import org.sipr.request.handler.SubscriptionRequest;

import javax.sip.InvalidArgumentException;
import javax.sip.message.Request;
import java.text.ParseException;

public interface NotifyContentBuilder {

    void addContent(Request primaryNotification, SubscriptionRequest request) throws ParseException, InvalidArgumentException;

    String getEventType();

}
