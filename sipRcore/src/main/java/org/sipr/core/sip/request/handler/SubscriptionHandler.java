package org.sipr.core.sip.request.handler;

import org.sipr.core.sip.request.processor.RequestException;

import javax.sip.RequestEvent;

public interface SubscriptionHandler {
    void handleRequest(RequestEvent requestEvent) throws RequestException;
}
