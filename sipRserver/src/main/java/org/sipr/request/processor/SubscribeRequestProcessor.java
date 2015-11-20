package org.sipr.request.processor;

import org.sipr.core.sip.request.processor.RequestProcessor;
import org.sipr.utils.SipMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Component
public class SubscribeRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        sipMessageSender.sendResponse(requestEvent, Response.NOT_IMPLEMENTED);
    }

    @Override
    public String getRequestType() {
        return Request.SUBSCRIBE;
    }
}
