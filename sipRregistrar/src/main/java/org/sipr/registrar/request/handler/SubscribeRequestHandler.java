package org.sipr.registrar.request.handler;

import akka.actor.UntypedActor;
import org.sipr.core.service.SipMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.message.Response;

import static java.lang.String.format;

@Component("subscribeHandler")
@Scope("prototype")
public class SubscribeRequestHandler extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeRequestHandler.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void onReceive(Object message) throws Exception {
        LOGGER.debug(format("Enter actor subscribe processing %s", self().path().name()));

        if (message instanceof RequestEvent) {
            processRequestEvent((RequestEvent) message);
        }

        getContext().stop(self());
        LOGGER.debug(format("Exit actor subscribe processing %s", self().path().name()));
    }

    void processRequestEvent(RequestEvent requestEvent) {
        sipMessageSender.sendResponse(requestEvent, Response.NOT_IMPLEMENTED);
    }
}
