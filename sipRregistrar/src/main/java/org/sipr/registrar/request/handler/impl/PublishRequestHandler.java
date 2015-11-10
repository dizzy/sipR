package org.sipr.registrar.request.handler.impl;

import org.sipr.core.service.SipMessageSender;
import org.sipr.registrar.request.handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.message.Response;

@Component("publishHandler")
@Scope("prototype")
public class PublishRequestHandler implements RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishRequestHandler.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void handle(RequestEvent requestEvent) {
        sipMessageSender.sendResponse(requestEvent, Response.NOT_IMPLEMENTED);
    }
}
