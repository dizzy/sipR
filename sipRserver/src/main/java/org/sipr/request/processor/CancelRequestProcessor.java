package org.sipr.request.processor;

import org.sipr.utils.SipMessageSender;
import org.sipr.core.sip.request.processor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Component
public class CancelRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        sipMessageSender.sendResponse(requestEvent, Response.NOT_IMPLEMENTED);
    }

    @Override
    public String getRequestType() {
        return Request.CANCEL;
    }
}
