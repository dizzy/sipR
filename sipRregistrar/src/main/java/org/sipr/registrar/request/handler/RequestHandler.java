package org.sipr.registrar.request.handler;

import javax.sip.RequestEvent;

public interface RequestHandler {
    void handle(RequestEvent event);
}
