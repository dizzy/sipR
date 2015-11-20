package org.sipr.core.sip.request.processor;

import javax.sip.RequestEvent;

public interface RequestProcessor {
    void processEvent(RequestEvent event);

    String getRequestType();
}
