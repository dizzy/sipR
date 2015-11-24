package org.sipr.request.processor;

import org.sipr.core.sip.request.handler.SubscriptionHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.processor.RequestProcessor;
import org.sipr.core.sip.request.validator.RequestValidator;
import org.sipr.utils.SipMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.List;

@Component
public class SubscribeRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Inject
    SubscriptionHandler subscriptionHandler;

    @Inject
    List<RequestValidator> requestValidators;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        try {

            // run request through validator chain
            for (RequestValidator requestValidator : requestValidators) {
                requestValidator.validateRequest(requestEvent);
            }

            subscriptionHandler.handleRequest(requestEvent);

        } catch (RequestException ex) {
            sipMessageSender.sendResponse(requestEvent, ex.getErrorCode(), ex.getHeaders());
        } catch (ParseException pex) {
            sipMessageSender.sendResponse(requestEvent, Response.SERVER_INTERNAL_ERROR);
        }
    }

    @Override
    public String getRequestType() {
        return Request.SUBSCRIBE;
    }
}
