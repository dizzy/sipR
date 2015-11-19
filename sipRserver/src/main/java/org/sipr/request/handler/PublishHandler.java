package org.sipr.request.handler;

import org.sipr.request.processor.RequestException;

import javax.sip.RequestEvent;

public interface PublishHandler {
    void handleRequest(RequestEvent requestEvent) throws RequestException;

    String getEventType();
}
