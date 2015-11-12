package org.sipr.core.service.impl;

import org.sipr.core.service.SipMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipProvider;
import javax.sip.header.Header;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;
import java.util.List;

@Component
public class SipMessageSenderImpl implements SipMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMessageSenderImpl.class);

    @Inject
    MessageFactory messageFactory;

    @Override
    public void sendResponse(RequestEvent requestEvent, Integer responseCode) {
        sendResponse(requestEvent, responseCode, null);
    }

    @Override
    public void sendResponse(RequestEvent requestEvent, Integer responseCode, List<? extends Header> headers) {
        ServerTransaction serverTransaction = requestEvent.getServerTransaction();
        SipProvider sipProvider = (SipProvider) requestEvent.getSource();
        try {
            Response response = messageFactory.createResponse(responseCode, requestEvent.getRequest());

            if (headers != null) {
                for (Header header : headers) {
                    response.addHeader(header);
                }
            }

            if (serverTransaction != null) {
                serverTransaction.sendResponse(response);
            } else {
                sipProvider.sendResponse(response);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to process message" + e.getMessage());
        }
    }
}
