package org.sipr.request.handler;

import javax.sip.RequestEvent;

public interface RequestHandler {
    void handle(RequestEvent event);
}
