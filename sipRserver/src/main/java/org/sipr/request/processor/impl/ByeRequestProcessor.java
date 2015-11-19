package org.sipr.request.processor.impl;

import org.sipr.core.service.SipMessageSender;
import org.sipr.request.processor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Component
public class ByeRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByeRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        sipMessageSender.sendResponse(requestEvent, Response.OK);
    }

    @Override
    public String getRequestType() {
        return Request.BYE;
    }
}
