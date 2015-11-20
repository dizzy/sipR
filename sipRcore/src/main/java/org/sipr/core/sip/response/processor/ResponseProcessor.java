package org.sipr.core.sip.response.processor;

import javax.sip.ResponseEvent;

public interface ResponseProcessor {
    void processEvent(ResponseEvent event);
}
