package org.sipr.request.processor;

import org.sipr.core.sip.request.handler.SubscriptionHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.processor.RequestProcessor;
import org.sipr.request.validator.AuthValidator;
import org.sipr.utils.SipMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.message.Request;

@Component
public class SubscribeRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Inject
    SubscriptionHandler subscriptionHandler;

    @Inject
    AuthValidator authValidator;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        try {
            // validate request
            authValidator.validateRequest(requestEvent);

            subscriptionHandler.handleRequest(requestEvent);

        } catch (RequestException ex) {
            sipMessageSender.sendResponse(requestEvent, ex.getErrorCode(), ex.getHeaders());
        }
    }

    @Override
    public String getRequestType() {
        return Request.SUBSCRIBE;
    }
}
