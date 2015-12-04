package org.sipr.core.sip.request.handler;

import javax.sip.RequestEvent;

public interface SubscriptionHandler {
    void handleRequest(RequestEvent requestEvent);
}
