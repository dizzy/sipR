package org.sipr.response.processor;

import org.sipr.core.sip.response.processor.ResponseProcessor;
import org.springframework.stereotype.Component;

import javax.sip.ResponseEvent;

@Component
public class ResponseProcessorImpl implements ResponseProcessor {
    @Override
    public void processEvent(ResponseEvent event) {

    }
}
