package org.sipr.request.handler.impl;

import org.sipr.request.handler.PublishHandler;
import org.sipr.request.processor.RequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.message.Request;

@Component
public class VqrCollectorImpl implements PublishHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VqrCollectorImpl.class);
    private static final Logger VQR_LOGGER = LoggerFactory.getLogger("vq-rtcpxr");

    @Override
    public void handleRequest(RequestEvent requestEvent) throws RequestException {
        Request publish = null;
        try {
            publish = requestEvent.getRequest();
            String content = new String(publish.getRawContent(), "UTF-8");
            VQR_LOGGER.info(content);
        } catch (Exception ex) {
            LOGGER.error("Failed to store event: " + publish.toString());
        }
    }

    @Override
    public String getEventType() {
        return "vq-rtcpxr";
    }
}
