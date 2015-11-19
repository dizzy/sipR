package org.sipr.request.processor.impl;

import org.sipr.core.service.SipMessageSender;
import org.sipr.request.handler.PublishHandler;
import org.sipr.request.processor.RequestException;
import org.sipr.request.processor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.header.EventHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
public class PublishRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Inject
    List<PublishHandler> publishHandlers;

    Map<String, PublishHandler> publishHandlersMap;

    @PostConstruct
    void init() {
        publishHandlersMap = publishHandlers.stream().collect(toMap(PublishHandler::getEventType, (r) -> r));
    }

    @Override
    public void processEvent(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        EventHeader header = (EventHeader) request.getHeader(EventHeader.NAME);

        try {
            PublishHandler handler = publishHandlersMap.get(header.getEventType());

            if (handler == null) {
                sipMessageSender.sendResponse(requestEvent, Response.NOT_IMPLEMENTED);
            }

            handler.handleRequest(requestEvent);

            sipMessageSender.sendResponse(requestEvent, Response.OK);

        } catch (RequestException ex) {
            sipMessageSender.sendResponse(requestEvent, ex.getErrorCode(), ex.getHeaders());
        }
    }

    @Override
    public String getRequestType() {
        return Request.PUBLISH;
    }
}
