package org.sipr.request.notify;

import org.sipr.request.handler.SubscriptionRequest;

import javax.sip.header.*;

public interface NotifySender {

    void sendNotifyToAllSubscribers(SubscriptionRequest request, ContentTypeHeader contentTypeHeader, String content);
}
